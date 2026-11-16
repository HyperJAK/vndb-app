package com.zc.vndb.feature.detail.presentation

import com.zc.vndb.common.ui.component.spoilerfilter.model.SpoilerLevel

sealed interface DetailIntent {
    data object Load : DetailIntent
    data object Retry : DetailIntent
    data object BackClicked : DetailIntent
    data object ShareClicked : DetailIntent
    data object OpenInBrowserClicked : DetailIntent
    data class LinkClicked(val url: String) : DetailIntent
    data class SpoilerLevelSelected(val level: SpoilerLevel) : DetailIntent
    data object SynopsisToggled : DetailIntent
    data class RelationClicked(val id: String) : DetailIntent
    data class CharacterClicked(val id: String) : DetailIntent
    data class TagClicked(val id: String) : DetailIntent
}
