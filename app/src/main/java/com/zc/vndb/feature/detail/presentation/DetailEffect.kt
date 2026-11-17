package com.zc.vndb.feature.detail.presentation

sealed interface DetailEffect {
    data object NavigateBack : DetailEffect
    data class Share(val title: String, val url: String) : DetailEffect
    data class OpenUrl(val url: String) : DetailEffect
    data class NavigateToNovel(val id: String) : DetailEffect
    data class NavigateToCharacter(val id: String) : DetailEffect
    data class NavigateToTag(val id: String) : DetailEffect
    data class NavigateToProducer(val id: String) : DetailEffect
    data class NavigateToRelease(val id: String) : DetailEffect
    data class OpenListEditor(val novelId: String, val title: String) : DetailEffect
}
