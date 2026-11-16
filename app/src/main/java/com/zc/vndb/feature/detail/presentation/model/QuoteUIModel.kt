package com.zc.vndb.feature.detail.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

@Immutable
data class QuoteUIModel(
    val id: String,
    val text: UiText,
    val characterId: String?,
    val characterName: UiText?,
    val scoreLabel: UiText,
)
