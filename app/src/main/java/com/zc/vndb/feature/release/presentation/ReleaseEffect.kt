package com.zc.vndb.feature.release.presentation

sealed interface ReleaseEffect {
    data object NavigateBack : ReleaseEffect
    data class Share(val title: String, val url: String) : ReleaseEffect
    data class OpenUrl(val url: String) : ReleaseEffect
    data class NavigateToNovel(val id: String) : ReleaseEffect
}
