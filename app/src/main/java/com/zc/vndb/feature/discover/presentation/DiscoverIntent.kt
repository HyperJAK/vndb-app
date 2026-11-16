package com.zc.vndb.feature.discover.presentation

sealed interface DiscoverIntent {
    data object Load : DiscoverIntent
    data object Retry : DiscoverIntent

    /** Pull to refresh: reloads every section while the current ones stay on screen. */
    data object Refresh : DiscoverIntent
    data object RandomRollClicked : DiscoverIntent
    data class NovelClicked(val id: String) : DiscoverIntent
    data object SeeAllTopRatedClicked : DiscoverIntent
    data object StatsClicked : DiscoverIntent
}
