package com.zc.vndb.feature.detail.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

/** A screenshot or cover thumbnail; [index] is its position in the viewer. */
@Immutable
data class MediaImageUIModel(
    val url: String,
    val thumbnailUrl: String?,
    val caption: UiText?,
    val isSensitive: Boolean,
    val index: Int,
)
