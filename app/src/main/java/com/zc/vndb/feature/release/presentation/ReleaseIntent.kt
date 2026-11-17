package com.zc.vndb.feature.release.presentation

sealed interface ReleaseIntent {
    data object Load : ReleaseIntent
    data object Retry : ReleaseIntent
    data object Refresh : ReleaseIntent
    data object BackClicked : ReleaseIntent
    data object ShareClicked : ReleaseIntent
    data object OpenInBrowserClicked : ReleaseIntent
    data class LinkClicked(val url: String) : ReleaseIntent
    data class NovelClicked(val id: String) : ReleaseIntent
    data class ProducerClicked(val id: String) : ReleaseIntent
    data object NotesToggled : ReleaseIntent
}
