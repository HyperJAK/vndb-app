package com.zc.vndb.feature.browse.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zc.vndb.common.ui.component.error.mapper.toErrorUIModel
import com.zc.vndb.feature.browse.domain.model.BrowseFilter
import com.zc.vndb.feature.browse.domain.model.BrowseSort
import com.zc.vndb.feature.browse.domain.usecase.SearchNovelsUseCase
import com.zc.vndb.feature.browse.presentation.component.browsenovelcard.mapper.toCardUIModel
import com.zc.vndb.feature.browse.presentation.mapper.toGenreChips
import com.zc.vndb.feature.browse.presentation.mapper.toResultCountLabel
import com.zc.vndb.feature.browse.presentation.mapper.toSortOptions
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BrowseViewModel(private val searchNovels: SearchNovelsUseCase) : ViewModel() {

    private val _state = MutableStateFlow(BrowseState())
    val state: StateFlow<BrowseState> = _state.asStateFlow()

    private val _effect = Channel<BrowseEffect>(Channel.BUFFERED)
    val effect: Flow<BrowseEffect> = _effect.receiveAsFlow()

    /** The filter the current results belong to. */
    private var filter = BrowseFilter()
    private var nextPage = SearchNovelsUseCase.FIRST_PAGE
    private var loadJob: Job? = null
    private var debounceJob: Job? = null

    init {
        onIntent(BrowseIntent.Load)
    }

    fun onIntent(intent: BrowseIntent) {
        when (intent) {
            BrowseIntent.Load,
            BrowseIntent.Retry,
            -> reload()

            is BrowseIntent.QueryChanged -> onQueryChanged(intent.query)

            BrowseIntent.SearchSubmitted -> applyFilter(filter.withQuery(_state.value.query))

            BrowseIntent.QueryCleared -> {
                _state.update { it.copy(query = "") }
                applyFilter(filter.withQuery(""))
            }

            is BrowseIntent.AgeRatingSelected -> applyFilter(
                filter.copy(ageRating = intent.ageRating),
            )

            is BrowseIntent.GenreToggled -> applyFilter(
                filter.copy(
                    genres = if (intent.genre in
                        filter.genres
                    ) {
                        filter.genres - intent.genre
                    } else {
                        filter.genres +
                            intent.genre
                    },
                ),
            )

            is BrowseIntent.SortSelected -> applyFilter(filter.copy(sort = intent.sort))

            BrowseIntent.EndReached -> if (!_state.value.loadMoreFailed) loadNextPage()

            BrowseIntent.LoadMoreRetried -> loadNextPage()

            is BrowseIntent.NovelClicked -> sendEffect(BrowseEffect.NavigateToNovel(intent.id))
        }
    }

    private fun onQueryChanged(query: String) {
        _state.update { it.copy(query = query) }
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MILLIS)
            applyFilter(filter.withQuery(query))
        }
    }

    private fun applyFilter(newFilter: BrowseFilter) {
        debounceJob?.cancel()
        if (newFilter == filter) return
        filter = newFilter
        reload()
    }

    /** Starts over from the first page of [filter]. */
    private fun reload() {
        loadJob?.cancel()
        nextPage = SearchNovelsUseCase.FIRST_PAGE
        _state.update {
            it.copy(
                ageRating = filter.ageRating,
                sort = filter.sort,
                sortOptions = filter.toSortOptions(),
                genres = filter.toGenreChips(),
                results = persistentListOf(),
                resultCountLabel = null,
                isLoading = true,
                isLoadingMore = false,
                canLoadMore = false,
                loadMoreFailed = false,
                error = null,
            )
        }
        loadJob = viewModelScope.launch {
            searchNovels(filter, nextPage)
                .onSuccess { page ->
                    nextPage++
                    _state.update {
                        it.copy(
                            isLoading = false,
                            results = page.novels.map { novel ->
                                novel.toCardUIModel()
                            }.toImmutableList(),
                            resultCountLabel = page.totalCount?.toResultCountLabel(),
                            canLoadMore = page.hasMore,
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.toErrorUIModel(),
                        )
                    }
                }
        }
    }

    private fun loadNextPage() {
        val current = _state.value
        if (current.isLoading || current.isLoadingMore || !current.canLoadMore) return
        _state.update { it.copy(isLoadingMore = true, loadMoreFailed = false) }
        loadJob = viewModelScope.launch {
            searchNovels(filter, nextPage)
                .onSuccess { page ->
                    nextPage++
                    _state.update { state ->
                        val known = state.results.mapTo(HashSet()) { it.id }
                        val added = page.novels.filter {
                            it.id !in known
                        }.map { it.toCardUIModel() }
                        state.copy(
                            isLoadingMore = false,
                            results = (state.results + added).toImmutableList(),
                            canLoadMore = page.hasMore,
                        )
                    }
                }
                .onFailure {
                    _state.update {
                        it.copy(
                            isLoadingMore = false,
                            loadMoreFailed = true,
                        )
                    }
                }
        }
    }

    private fun sendEffect(effect: BrowseEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }

    /**
     * Searching switches the default Popularity sort to Relevance; clearing the search
     * switches Relevance back to Popularity.
     */
    private fun BrowseFilter.withQuery(rawQuery: String): BrowseFilter {
        val newQuery = rawQuery.trim()
        val newSort = when {
            newQuery.isNotEmpty() && query.isEmpty() && sort == BrowseSort.Popularity -> BrowseSort.Relevance
            newQuery.isEmpty() && sort == BrowseSort.Relevance -> BrowseSort.Popularity
            else -> sort
        }
        return copy(query = newQuery, sort = newSort)
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MILLIS = 400L
    }
}
