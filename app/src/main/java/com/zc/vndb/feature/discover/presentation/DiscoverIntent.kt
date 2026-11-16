package com.zc.vndb.feature.discover.presentation

sealed interface DiscoverIntent {
    data object Load : DiscoverIntent
    data object Retry : DiscoverIntent
    data object RandomRollClicked : DiscoverIntent
    data class NovelClicked(val id: String) : DiscoverIntent
    data object SeeAllTopRatedClicked : DiscoverIntent
    data object StatsClicked : DiscoverIntent
}
