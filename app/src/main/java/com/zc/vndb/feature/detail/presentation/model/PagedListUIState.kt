package com.zc.vndb.feature.detail.presentation.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

/** A list loaded page by page with "Load more". */
@Immutable
data class PagedListUIState<T>(
    val items: ImmutableList<T>,
    val hasMore: Boolean,
    val isLoadingMore: Boolean = false,
    val loadMoreFailed: Boolean = false,
)
