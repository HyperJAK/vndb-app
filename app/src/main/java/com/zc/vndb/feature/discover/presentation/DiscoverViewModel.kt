package com.zc.vndb.feature.discover.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zc.vndb.common.ui.component.error.mapper.toErrorUIModel
import com.zc.vndb.feature.discover.domain.model.DiscoverNovel
import com.zc.vndb.feature.discover.domain.usecase.GetDatabaseStatsUseCase
import com.zc.vndb.feature.discover.domain.usecase.GetFeaturedNovelUseCase
import com.zc.vndb.feature.discover.domain.usecase.GetTopRatedNovelsUseCase
import com.zc.vndb.feature.discover.domain.usecase.GetTrendingReleasesUseCase
import com.zc.vndb.feature.discover.domain.usecase.RollRandomNovelUseCase
import com.zc.vndb.feature.discover.presentation.component.featurednovel.mapper.toUIModel
import com.zc.vndb.feature.discover.presentation.component.novelcard.mapper.toNovelCardUIModel
import com.zc.vndb.feature.discover.presentation.component.trendingrelease.mapper.toUIModel
import com.zc.vndb.feature.discover.presentation.mapper.toCountLabel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiscoverViewModel(
    private val getTopRatedNovels: GetTopRatedNovelsUseCase,
    private val getFeaturedNovel: GetFeaturedNovelUseCase,
    private val getTrendingReleases: GetTrendingReleasesUseCase,
    private val getDatabaseStats: GetDatabaseStatsUseCase,
    private val rollRandomNovel: RollRandomNovelUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DiscoverState())
    val state: StateFlow<DiscoverState> = _state.asStateFlow()

    private val _effect = Channel<DiscoverEffect>(Channel.BUFFERED)
    val effect: Flow<DiscoverEffect> = _effect.receiveAsFlow()

    private var loadJob: Job? = null

    init {
        onIntent(DiscoverIntent.Load)
    }

    fun onIntent(intent: DiscoverIntent) {
        when (intent) {
            DiscoverIntent.Load,
            DiscoverIntent.Retry,
            -> load()

            DiscoverIntent.RandomRollClicked -> roll()

            is DiscoverIntent.NovelClicked -> sendEffect(DiscoverEffect.NavigateToNovel(intent.id))

            DiscoverIntent.SeeAllTopRatedClicked,
            DiscoverIntent.StatsClicked,
            -> sendEffect(DiscoverEffect.NavigateToBrowse)
        }
    }

    /**
     * Loads every section in parallel. Only a Top Rated failure is a screen error (nothing useful
     * to show); a failed hero, trending list or stats line just leaves that section out.
     */
    private fun load() {
        loadJob?.cancel()
        _state.update { it.copy(isLoading = true, error = null) }
        loadJob = viewModelScope.launch {
            val trending = async { getTrendingReleases() }
            val stats = async { getDatabaseStats() }
            val topRated = getTopRatedNovels().getOrElse { error ->
                trending.cancel()
                stats.cancel()
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = error.toErrorUIModel(),
                        featured = null,
                        topRated = persistentListOf(),
                        trending = persistentListOf(),
                        statsLabel = null,
                    )
                }
                return@launch
            }
            val featured = getFeaturedNovel(topRated).getOrNull()
            val trendingRows = trending.await().getOrNull().orEmpty()
            val statsLabel = stats.await().getOrNull()?.toCountLabel()
            _state.update {
                it.copy(
                    isLoading = false,
                    featured = featured?.toUIModel(),
                    topRated = topRated.toTopRatedCards(),
                    trending = trendingRows.map { row -> row.toUIModel() }.toImmutableList(),
                    statsLabel = statsLabel,
                )
            }
        }
    }

    private fun roll() {
        if (_state.value.isRolling) return
        _state.update { it.copy(isRolling = true) }
        viewModelScope.launch {
            rollRandomNovel()
                .onSuccess { id -> sendEffect(DiscoverEffect.NavigateToNovel(id)) }
                .onFailure { error -> sendEffect(DiscoverEffect.ShowError(error.toErrorUIModel())) }
            _state.update { it.copy(isRolling = false) }
        }
    }

    private fun sendEffect(effect: DiscoverEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }

    private fun List<DiscoverNovel>.toTopRatedCards() =
        take(TOP_RATED_ROW_SIZE).map { it.toNovelCardUIModel() }.toImmutableList()

    private companion object {
        /** The row shows the best few; the whole pool only feeds the weekly pick. */
        const val TOP_RATED_ROW_SIZE = 10
    }
}
