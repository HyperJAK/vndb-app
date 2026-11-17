package com.zc.vndb.feature.character.presentation

import com.zc.vndb.common.ui.component.spoilerfilter.model.SpoilerLevel

sealed interface CharacterIntent {
    data object Load : CharacterIntent
    data object Retry : CharacterIntent
    data object Refresh : CharacterIntent
    data object BackClicked : CharacterIntent
    data object ShareClicked : CharacterIntent
    data object OpenInBrowserClicked : CharacterIntent
    data class SpoilerLevelSelected(val level: SpoilerLevel) : CharacterIntent
    data object DescriptionToggled : CharacterIntent
    data class NovelClicked(val id: String) : CharacterIntent
    data class StaffClicked(val id: String) : CharacterIntent
}
