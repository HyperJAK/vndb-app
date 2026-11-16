package com.zc.vndb.feature.browse.presentation

import app.cash.turbine.test
import com.zc.vndb.R
import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.util.MainDispatcherRule
import com.zc.vndb.feature.browse.domain.model.AgeRating
import com.zc.vndb.feature.browse.domain.model.BrowseFilter
import com.zc.vndb.feature.browse.domain.model.BrowseSamples
import com.zc.vndb.feature.browse.domain.model.BrowseSort
import com.zc.vndb.feature.browse.domain.model.Genre
import com.zc.vndb.feature.browse.domain.repository.FakeBrowseRepository
import com.zc.vndb.feature.browse.domain.usecase.SearchNovelsUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Rule

class BrowseViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeBrowseRepository().apply {
        novels = (1..45).map { BrowseSamples.novel("v$it") }
    }

    private fun createViewModel() = BrowseViewModel(SearchNovelsUseCase(repository))

    @Test
    fun `loads the first page with the default filter`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(BrowseFilter(), repository.requests.single().filter)
        assertEquals(20, state.results.size)
        assertEquals(
            UiText.StringPlural(R.plurals.browse_found_titles, 45, listOf("%,d".format(45))),
            state.resultCountLabel,
        )
        assertTrue(state.canLoadMore)
        assertEquals(AgeRating.AllAges, state.ageRating)
        assertEquals(BrowseSort.Popularity, state.sort)
    }

    @Test
    fun `typing searches after a pause, switching to relevance`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onIntent(BrowseIntent.QueryChanged("ste"))
        viewModel.onIntent(BrowseIntent.QueryChanged("steins"))
        advanceTimeBy(399)
        runCurrent()
        assertEquals(1, repository.requests.size)
        assertEquals("steins", viewModel.state.value.query)

        advanceTimeBy(2)
        advanceUntilIdle()
        assertEquals(2, repository.requests.size)
        assertEquals(
            BrowseFilter(query = "steins", sort = BrowseSort.Relevance),
            repository.requests.last().filter,
        )
        assertEquals(BrowseSort.Relevance, viewModel.state.value.sort)
    }

    @Test
    fun `submitting searches at once and clearing restores popularity`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onIntent(BrowseIntent.QueryChanged("steins"))
        viewModel.onIntent(BrowseIntent.SearchSubmitted)
        runCurrent()
        assertEquals("steins", repository.requests.last().filter.query)

        viewModel.onIntent(BrowseIntent.QueryCleared)
        advanceUntilIdle()
        assertEquals(BrowseFilter(), repository.requests.last().filter)
        assertEquals("", viewModel.state.value.query)
        assertEquals(3, repository.requests.size)
    }

    @Test
    fun `filters reload from the first page`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onIntent(BrowseIntent.AgeRatingSelected(AgeRating.Adult))
        viewModel.onIntent(BrowseIntent.GenreToggled(Genre.Mystery))
        viewModel.onIntent(BrowseIntent.SortSelected(BrowseSort.Rating))
        advanceUntilIdle()

        val last = repository.requests.last()
        assertEquals(
            BrowseFilter(
                ageRating = AgeRating.Adult,
                genres = setOf(Genre.Mystery),
                sort = BrowseSort.Rating,
            ),
            last.filter,
        )
        assertEquals(1, last.page)
        val state = viewModel.state.value
        assertEquals(AgeRating.Adult, state.ageRating)
        assertEquals(listOf(Genre.Mystery), state.genres.filter { it.isSelected }.map { it.genre })

        viewModel.onIntent(BrowseIntent.GenreToggled(Genre.Mystery))
        advanceUntilIdle()
        assertTrue(repository.requests.last().filter.genres.isEmpty())
    }

    @Test
    fun `selecting the current filter again does not reload`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onIntent(BrowseIntent.AgeRatingSelected(AgeRating.AllAges))
        viewModel.onIntent(BrowseIntent.SortSelected(BrowseSort.Popularity))
        advanceUntilIdle()

        assertEquals(1, repository.requests.size)
    }

    @Test
    fun `reaching the end appends pages until there are no more`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onIntent(BrowseIntent.EndReached)
        advanceUntilIdle()
        assertEquals(40, viewModel.state.value.results.size)

        viewModel.onIntent(BrowseIntent.EndReached)
        advanceUntilIdle()
        assertEquals(45, viewModel.state.value.results.size)
        assertFalse(viewModel.state.value.canLoadMore)

        viewModel.onIntent(BrowseIntent.EndReached)
        advanceUntilIdle()
        assertEquals(listOf(1, 2, 3), repository.requests.map { it.page })
    }

    @Test
    fun `a failed next page waits for an explicit retry`() = runTest {
        repository.errorOnPage = 2
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onIntent(BrowseIntent.EndReached)
        advanceUntilIdle()
        assertTrue(viewModel.state.value.loadMoreFailed)
        assertEquals(20, viewModel.state.value.results.size)

        viewModel.onIntent(BrowseIntent.EndReached)
        advanceUntilIdle()
        assertEquals(2, repository.requests.size)

        repository.errorOnPage = null
        viewModel.onIntent(BrowseIntent.LoadMoreRetried)
        advanceUntilIdle()
        assertFalse(viewModel.state.value.loadMoreFailed)
        assertEquals(40, viewModel.state.value.results.size)
    }

    @Test
    fun `a failed first page shows an error and retry reloads`() = runTest {
        repository.error = DomainError.Network
        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(
            UiText.StringResource(R.string.error_network),
            viewModel.state.value.error?.message,
        )
        assertTrue(viewModel.state.value.results.isEmpty())

        repository.error = null
        viewModel.onIntent(BrowseIntent.Retry)
        advanceUntilIdle()
        assertEquals(null, viewModel.state.value.error)
        assertEquals(20, viewModel.state.value.results.size)
    }

    @Test
    fun `no matches shows the empty state`() = runTest {
        repository.novels = emptyList()
        val viewModel = createViewModel()
        advanceUntilIdle()

        assertTrue(viewModel.state.value.showEmpty)
    }

    @Test
    fun `clicking a result opens it`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onIntent(BrowseIntent.NovelClicked("v7"))
            assertEquals(BrowseEffect.NavigateToNovel("v7"), awaitItem())
        }
    }
}
