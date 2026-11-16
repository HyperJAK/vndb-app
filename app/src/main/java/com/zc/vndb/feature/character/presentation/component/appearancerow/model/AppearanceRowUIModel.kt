package com.zc.vndb.feature.character.presentation.component.appearancerow.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

@Immutable
data class AppearanceRowUIModel(
    val novelId: String,
    val title: UiText,
    val yearLabel: UiText,
    val roleLabel: UiText,
    val isMajorRole: Boolean,
    val voiceActorLabel: UiText?,
    val ratingLabel: UiText?,
    val thumbnailUrl: String?,
    val isCoverSensitive: Boolean,
)
