package com.zc.vndb.feature.browse.presentation

sealed interface BrowseEffect {
    data class NavigateToNovel(val id: String) : BrowseEffect
    data object NavigateBack : BrowseEffect
}
