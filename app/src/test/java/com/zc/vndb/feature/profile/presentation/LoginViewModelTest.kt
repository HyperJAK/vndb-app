package com.zc.vndb.feature.profile.presentation

import app.cash.turbine.test
import com.zc.vndb.R
import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.util.MainDispatcherRule
import com.zc.vndb.feature.profile.domain.model.ProfileSamples
import com.zc.vndb.feature.profile.domain.repository.FakeSessionRepository
import com.zc.vndb.feature.profile.domain.usecase.LogInUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeSessionRepository().apply {
        acceptedSession =
            ProfileSamples.session()
    }
    private val viewModel = LoginViewModel(LogInUseCase(repository))

    @Test
    fun `connect is enabled once something is typed`() {
        assertFalse(viewModel.state.value.canSubmit)
        viewModel.onIntent(LoginIntent.TokenChanged("x"))
        assertTrue(viewModel.state.value.canSubmit)
        viewModel.onIntent(LoginIntent.TokenVisibilityToggled)
        assertTrue(viewModel.state.value.isTokenVisible)
    }

    @Test
    fun `a valid token logs in and leaves the screen`() = runTest {
        viewModel.effect.test {
            viewModel.onIntent(LoginIntent.TokenChanged(ProfileSamples.TOKEN))
            viewModel.onIntent(LoginIntent.SubmitClicked)
            assertTrue(viewModel.state.value.isSubmitting)
            assertEquals(LoginEffect.LoggedIn, awaitItem())
        }
        assertFalse(viewModel.state.value.isSubmitting)
        assertEquals(ProfileSamples.session(), repository.current.value)
    }

    @Test
    fun `a malformed token shows a format hint without calling VNDB`() = runTest {
        viewModel.onIntent(LoginIntent.TokenChanged("my password"))
        viewModel.onIntent(LoginIntent.SubmitClicked)
        advanceUntilIdle()

        assertEquals(
            UiText.StringResource(R.string.login_error_format),
            viewModel.state.value.error,
        )
        assertTrue(repository.loggedInTokens.isEmpty())
    }

    @Test
    fun `a rejected token explains it and editing clears the error`() = runTest {
        repository.logInError = DomainError.Unauthorized
        viewModel.onIntent(LoginIntent.TokenChanged(ProfileSamples.TOKEN))
        viewModel.onIntent(LoginIntent.SubmitClicked)
        advanceUntilIdle()

        assertEquals(
            UiText.StringResource(R.string.login_error_invalid),
            viewModel.state.value.error,
        )
        viewModel.onIntent(LoginIntent.TokenChanged("${ProfileSamples.TOKEN} "))
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `links and navigation become effects`() = runTest {
        viewModel.effect.test {
            viewModel.onIntent(LoginIntent.OpenTokensPageClicked)
            assertEquals(LoginEffect.OpenUrl("https://vndb.org/u/tokens"), awaitItem())
            viewModel.onIntent(LoginIntent.ContinueAsGuestClicked)
            assertEquals(LoginEffect.NavigateBack, awaitItem())
        }
    }
}
