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
    /** The voice actor's staff id; the "CV:" line opens their page. */
    val voiceActorId: String? = null,
    val imageUrl: String?,
    val isImageSensitive: Boolean,
)
