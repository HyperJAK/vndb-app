package com.zc.vndb.feature.detail.presentation.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

/** The full-screen viewer, open on [startIndex]. */
@Immutable
data class ImageViewerUIState(val images: ImmutableList<ViewerImageUIModel>, val startIndex: Int)
