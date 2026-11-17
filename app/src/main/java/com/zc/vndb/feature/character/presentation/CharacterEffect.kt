package com.zc.vndb.feature.character.presentation

sealed interface CharacterEffect {
    data object NavigateBack : CharacterEffect
    data class Share(val name: String, val url: String) : CharacterEffect
    data class OpenUrl(val url: String) : CharacterEffect
    data class NavigateToNovel(val id: String) : CharacterEffect
    data class NavigateToStaff(val id: String) : CharacterEffect
}
