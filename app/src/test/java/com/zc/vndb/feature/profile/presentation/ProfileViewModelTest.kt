package com.zc.vndb.feature.profile.presentation

import app.cash.turbine.test
import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.domain.repository.FakeSessionRepository
import com.zc.vndb.common.domain.repository.FakeSettingsRepository
import com.zc.vndb.common.domain.usecase.ObserveSettingsUseCase
import com.zc.vndb.common.domain.util.FixedClock
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.util.MainDispatcherRule
import com.zc.vndb.feature.profile.domain.model.ListLabel
import com.zc.vndb.feature.profile.domain.model.ProfileSamples
import com.zc.vndb.feature.profile.domain.repository.FakeProfileRepository
import com.zc.vndb.feature.profile.domain.usecase.GetListEntriesUseCase
import com.zc.vndb.feature.profile.domain.usecase.GetProfileUseCase
import com.zc.vndb.feature.profile.domain.usecase.LogOutUseCase
import com.zc.vndb.feature.profile.domain.usecase.ObserveSessionUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Instant
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Rule

class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val sessions = FakeSessionRepository()
    private val profile = FakeProfileRepository().apply {
        labels = listOf(
            ProfileSamples.label(ListLabel.FINISHED, count = 2),
            ProfileSamples.label(ListLabel.PLAYING, count = 30),
        )
        entries[ListLabel.PLAYING] = (1..30).map { ProfileSamples.entry("v$it") }
        entries[ListLabel.FINISHED] = listOf(
            ProfileSamples.entry("v100"),
            ProfileSamples.entry("v101"),
        )
    }

    private val settings = FakeSettingsRepository()

    private fun createViewModel() = ProfileViewModel(
        observeSession = ObserveSessionUseCase(sessions),
        getProfile = GetProfileUseCase(profile),
        getListEntries = GetListEntriesUseCase(profile),
        logOut = LogOutUseCase(sessions),
        clock = FixedClock(Instant.parse("2026-11-16T00:00:00Z")),
        observeSettings = ObserveSettingsUseCase(settings),
    )

    /** `state` only runs while collected, as on screen. */
    private fun TestScope.collect(state: StateFlow<ProfileState>) {
        backgroundScope.launch { state.collect {} }
    }

    @Test
    fun `a guest sees the log-in prompt and nothing is requested`() = runTest {
        val viewModel = createViewModel()
        collect(viewModel.state)
        advanceUntilIdle()

        with(viewModel.state.value) {
            assertTrue(isSessionKnown)
            assertFalse(isSignedIn)
        }
        assertTrue(profile.calls.isEmpty())

        viewModel.effect.test {
            viewModel.onIntent(ProfileIntent.LogInClicked)
            assertEquals(ProfileEffect.NavigateToLogin, awaitItem())
        }
    }

    @Test
    fun `signing in loads the labels and Playing's first page`() = runTest {
        val viewModel = createViewModel()
        collect(viewModel.state)
        advanceUntilIdle()

        sessions.current.value = ProfileSamples.session()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(UiText.StringValue("Yorhel"), state.header?.username)
        assertEquals(listOf(ListLabel.PLAYING, ListLabel.FINISHED), state.labels.map { it.id })
        assertEquals(ListLabel.PLAYING, state.labels.single { it.isSelected }.id)
        assertEquals(25, state.entries.items.size)
        assertTrue(state.entries.hasMore)
        assertTrue("entries:1:1" in profile.calls)
    }

    @Test
    fun `labels load once and are kept, more entries load at the end`() = runTest {
        sessions.current.value = ProfileSamples.session()
        val viewModel = createViewModel()
        collect(viewModel.state)
        advanceUntilIdle()

        viewModel.onIntent(ProfileIntent.LabelSelected(ListLabel.FINISHED))
        advanceUntilIdle()
        assertEquals(listOf("v100", "v101"), viewModel.state.value.entries.items.map { it.novelId })

        viewModel.onIntent(ProfileIntent.LabelSelected(ListLabel.PLAYING))
        viewModel.onIntent(ProfileIntent.EndReached)
        advanceUntilIdle()

        assertEquals(30, viewModel.state.value.entries.items.size)
        assertFalse(viewModel.state.value.entries.hasMore)
        assertEquals(
            listOf("entries:1:1", "entries:2:1", "entries:1:2"),
            profile.calls.filter { it.startsWith("entries") },
        )
    }

    @Test
    fun `a failed overview is a retryable error`() = runTest {
        sessions.current.value = ProfileSamples.session()
        profile.labelsError = DomainError.Network
        val viewModel = createViewModel()
        collect(viewModel.state)
        advanceUntilIdle()
        assertNotNull(viewModel.state.value.error)

        profile.labelsError = null
        viewModel.onIntent(ProfileIntent.Retry)
        advanceUntilIdle()
        assertNull(viewModel.state.value.error)
        assertEquals(25, viewModel.state.value.entries.items.size)
    }

    @Test
    fun `a rejected token signs out with a message`() = runTest {
        sessions.current.value = ProfileSamples.session()
        profile.labelsError = DomainError.Unauthorized
        val viewModel = createViewModel()
        collect(viewModel.state)

        viewModel.effect.test {
            advanceUntilIdle()
            assertEquals(
                ProfileEffect.ShowMessage(
                    UiText.StringResource(StringResource.profile_session_expired),
                ),
                awaitItem(),
            )
        }
        // The real repository clears the token; the session flow then turns the screen into a guest.
        sessions.current.value = null
        advanceUntilIdle()
        assertFalse(viewModel.state.value.isSignedIn)
    }

    @Test
    fun `log out asks first, then returns to the guest prompt`() = runTest {
        sessions.current.value = ProfileSamples.session()
        val viewModel = createViewModel()
        collect(viewModel.state)
        advanceUntilIdle()

        viewModel.onIntent(ProfileIntent.LogOutClicked)
        runCurrent()
        assertTrue(viewModel.state.value.isLogOutDialogVisible)
        viewModel.onIntent(ProfileIntent.LogOutConfirmed)
        advanceUntilIdle()

        assertFalse(viewModel.state.value.isSignedIn)
        assertFalse(viewModel.state.value.isLogOutDialogVisible)
        assertNull(sessions.current.value)
    }

    @Test
    fun `pull to refresh reloads labels and the selected label`() = runTest {
        sessions.current.value = ProfileSamples.session()
        val viewModel = createViewModel()
        collect(viewModel.state)
        advanceUntilIdle()
        profile.calls.clear()

        viewModel.onIntent(ProfileIntent.Refresh)
        advanceUntilIdle()

        assertFalse(viewModel.state.value.isRefreshing)
        assertEquals(25, viewModel.state.value.entries.items.size)
        assertTrue(profile.calls.containsAll(listOf("labels", "user", "entries:1:1")))
    }
}
