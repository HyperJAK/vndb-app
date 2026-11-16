package com.zc.vndb.feature.detail.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

/** One screenshot toggle: "Safe 8". */
@Immutable
data class ContentToggleUIState(
    val filter: ContentFilter,
    val label: UiText,
    val isSelected: Boolean,
)
