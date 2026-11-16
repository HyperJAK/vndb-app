package com.zc.vndb.feature.detail.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

@Immutable
data class DetailTabUIModel(
    val tab: DetailTab,
    /** "Media", or "Media 10" once the tab's data has loaded. */
    val label: UiText,
)
