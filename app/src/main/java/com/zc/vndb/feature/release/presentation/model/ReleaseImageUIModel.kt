package com.zc.vndb.feature.release.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

@Immutable
data class ReleaseImageUIModel(
    val url: String,
    val thumbnailUrl: String?,
    val typeLabel: UiText,
    val isSensitive: Boolean,
)
