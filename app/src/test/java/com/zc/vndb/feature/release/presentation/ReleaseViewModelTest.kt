package com.zc.vndb.feature.release.presentation

import app.cash.turbine.test
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.util.MainDispatcherRule
import com.zc.vndb.feature.release.domain.model.ReleaseSamples
import com.zc.vndb.feature.release.domain.repository.FakeReleaseRepository
import com.zc.vndb.feature.release.domain.usecase.GetReleaseUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule

class ReleaseViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeReleaseRepository().apply {
        releases["r7668"] =
            ReleaseSamples.release()
    }

    private fun createViewModel() = ReleaseViewModel(
        releaseId = "r7668",
        getRelease = GetReleaseUseCase(repository),
    )

    @Test
    fun `loads every section`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        with(viewModel.state.value) {
            assertFalse(isLoading)
            assertEquals(UiText.StringValue("Steins;Gate"), hero?.title)
            assertEquals("https://vndb.org/r7668", links.first().url)
            assertEquals(listOf("v2002"), novels.map { it.id })
            assertEquals(1, languages.size)
            assertEquals(1, producers.size)
            assertNotNull(notes)
        }
    }

    @Test
    fun `a missing release is an error and retry reloads`() = runTest {
        val saved = repository.releases.remove("r7668")
        val viewModel = createViewModel()
        advanceUntilIdle()
        assertEquals(
            UiText.StringResource(StringResource.error_not_found),
            viewModel.state.value.error?.message,
        )

        repository.releases["r7668"] = checkNotNull(saved)
        viewModel.onIntent(ReleaseIntent.Retry)
        advanceUntilIdle()
        assertNull(viewModel.state.value.error)
        assertNotNull(viewModel.state.value.hero)
    }

    @Test
    fun `notes toggle and actions become effects`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onIntent(ReleaseIntent.NotesToggled)
        assertTrue(viewModel.state.value.isNotesExpanded)

        viewModel.effect.test {
            viewModel.onIntent(ReleaseIntent.BackClicked)
            assertEquals(ReleaseEffect.NavigateBack, awaitItem())
            viewModel.onIntent(ReleaseIntent.ShareClicked)
            assertEquals(ReleaseEffect.Share("Steins;Gate", "https://vndb.org/r7668"), awaitItem())
            viewModel.onIntent(ReleaseIntent.OpenInBrowserClicked)
            assertEquals(ReleaseEffect.OpenUrl("https://vndb.org/r7668"), awaitItem())
            viewModel.onIntent(ReleaseIntent.LinkClicked("http://steinsgate.jp/"))
            assertEquals(ReleaseEffect.OpenUrl("http://steinsgate.jp/"), awaitItem())
            viewModel.onIntent(ReleaseIntent.NovelClicked("v2002"))
            assertEquals(ReleaseEffect.NavigateToNovel("v2002"), awaitItem())
        }
    }
}
