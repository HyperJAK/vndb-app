package com.zc.vndb.feature.release.presentation.component.releasenovelrow.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

@Immutable
data class ReleaseNovelRowUIModel(
    val id: String,
    val title: UiText,
    val yearLabel: UiText,
    val typeLabel: UiText,
    val isComplete: Boolean,
    val thumbnailUrl: String?,
    val isCoverSensitive: Boolean,
)
