package com.zc.vndb.feature.detail.presentation.component.charactercard.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

@Immutable
data class CharacterCardUIModel(
    val id: String,
    val name: UiText,
    val originalName: UiText?,
    val roleLabel: UiText,
    val isMain: Boolean,
    val voiceActorLabel: UiText?,
    val imageUrl: String?,
    val isImageSensitive: Boolean,
)
