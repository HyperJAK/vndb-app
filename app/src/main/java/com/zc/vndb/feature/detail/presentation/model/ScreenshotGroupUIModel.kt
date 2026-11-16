package com.zc.vndb.feature.detail.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText
import kotlinx.collections.immutable.ImmutableList

/** Screenshots from one release. */
@Immutable
data class ScreenshotGroupUIModel(
    val title: UiText?,
    val screenshots: ImmutableList<MediaImageUIModel>,
)
