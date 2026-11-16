package com.zc.vndb.feature.detail.presentation.component.relationcard.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

@Immutable
data class RelationCardUIModel(
    val id: String,
    val title: UiText,
    val relationLabel: UiText,
    val isOfficial: Boolean,
    val yearLabel: UiText?,
    val scoreLabel: UiText?,
)
