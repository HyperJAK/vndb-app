package com.zc.vndb.feature.detail.presentation

import app.cash.turbine.test
import com.zc.vndb.R
import com.zc.vndb.common.domain.model.CharacterRole
import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.ui.component.spoilerfilter.model.SpoilerLevel
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.util.MainDispatcherRule
import com.zc.vndb.feature.detail.domain.model.DetailSamples
import com.zc.vndb.feature.detail.domain.repository.FakeDetailRepository
import com.zc.vndb.feature.detail.domain.usecase.GetNovelCharactersUseCase
import com.zc.vndb.feature.detail.domain.usecase.GetNovelDetailUseCase
import com.zc.vndb.feature.detail.domain.usecase.GetNovelEditionsUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule

class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeDetailRepository().apply {
        novels["v2002"] = DetailSamples.novel(
            description = "Intro. [spoiler]Twist[/spoiler]",
            tags = listOf(DetailSamples.tag("Visible"), DetailSamples.tag("Hidden", spoiler = 2)),
            relations = listOf(DetailSamples.relation("v17102")),
            voiceActors = mapOf("c1" to "Miyano Mamoru"),
        )
        characters = listOf(
            DetailSamples.character("c1", CharacterRole.Main),
            DetailSamples.character("c2", CharacterRole.Primary, spoiler = 1),
        )
        editions = listOf(DetailSamples.edition("r1", minAge = 18))
    }

    private fun createViewModel() = DetailViewModel(
        novelId = "v2002",
        getNovelDetail = GetNovelDetailUseCase(repository),
        getNovelCharacters = GetNovelCharactersUseCase(repository),
        getNovelEditions = GetNovelEditionsUseCase(repository),
    )

    @Test
    fun `loads every section`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals(UiText.StringValue("STEINS;GATE"), state.hero?.title)
        assertEquals(
            UiText.StringResource(R.string.detail_age_min, listOf(18)),
            state.hero?.ageLabel,
        )
        assertNotNull(state.score)
        assertEquals("https://vndb.org/v2002", state.links.first().url)
        assertEquals(listOf("r1-win"), state.editions.map { it.key })
        assertEquals(listOf("v17102"), state.relations.map { it.id })
        assertEquals(
            UiText.StringResource(R.string.detail_cv, listOf("Miyano Mamoru")),
            state.characters.first().voiceActorLabel,
        )
    }

    @Test
    fun `spoiler filter hides spoiler content until raised`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        with(viewModel.state.value) {
            assertEquals(UiText.StringValue("Intro."), synopsis)
            assertEquals(listOf("c1"), characters.map { it.id })
            assertNotNull(hiddenCharactersLabel)
            assertNotNull(hiddenTagsLabel)
        }

        viewModel.onIntent(DetailIntent.SpoilerLevelSelected(SpoilerLevel.Major))

        with(viewModel.state.value) {
            assertEquals(SpoilerLevel.Major, spoilerLevel)
            assertEquals(UiText.StringValue("Intro. Twist"), synopsis)
            assertEquals(listOf("c1", "c2"), characters.map { it.id })
            assertNull(hiddenCharactersLabel)
            assertNull(hiddenTagsLabel)
        }
    }

    @Test
    fun `failed characters and editions only hide their sections`() = runTest {
        repository.charactersError = DomainError.Server
        repository.editionsError = DomainError.Timeout
        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertNull(state.error)
        assertNotNull(state.hero)
        assertTrue(state.characters.isEmpty() && state.editions.isEmpty())
        assertNull(state.hero?.ageLabel)
    }

    @Test
    fun `a missing novel is an error and retry reloads`() = runTest {
        val saved = repository.novels.remove("v2002")
        val viewModel = createViewModel()
        advanceUntilIdle()
        assertEquals(
            UiText.StringResource(R.string.error_not_found),
            viewModel.state.value.error?.message,
        )

        repository.novels["v2002"] = checkNotNull(saved)
        viewModel.onIntent(DetailIntent.Retry)
        advanceUntilIdle()
        assertNull(viewModel.state.value.error)
        assertNotNull(viewModel.state.value.hero)
    }

    @Test
    fun `synopsis toggles`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onIntent(DetailIntent.SynopsisToggled)
        assertTrue(viewModel.state.value.isSynopsisExpanded)
    }

    @Test
    fun `actions become effects`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onIntent(DetailIntent.BackClicked)
            assertEquals(DetailEffect.NavigateBack, awaitItem())
            viewModel.onIntent(DetailIntent.ShareClicked)
            assertEquals(DetailEffect.Share("STEINS;GATE", "https://vndb.org/v2002"), awaitItem())
            viewModel.onIntent(DetailIntent.OpenInBrowserClicked)
            assertEquals(DetailEffect.OpenUrl("https://vndb.org/v2002"), awaitItem())
            viewModel.onIntent(DetailIntent.LinkClicked("https://en.wikipedia.org"))
            assertEquals(DetailEffect.OpenUrl("https://en.wikipedia.org"), awaitItem())
            viewModel.onIntent(DetailIntent.RelationClicked("v17102"))
            assertEquals(DetailEffect.NavigateToNovel("v17102"), awaitItem())
            viewModel.onIntent(DetailIntent.CharacterClicked("c6487"))
            assertEquals(DetailEffect.NavigateToCharacter("c6487"), awaitItem())
        }
    }
}
