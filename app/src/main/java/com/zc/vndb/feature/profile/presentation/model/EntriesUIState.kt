package com.zc.vndb.feature.profile.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.component.error.model.ErrorUIModel
import com.zc.vndb.feature.profile.presentation.component.listentrycard.model.ListEntryCardUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/** Entries of the selected label. */
@Immutable
data class EntriesUIState(
    val items: ImmutableList<ListEntryCardUIModel> = persistentListOf(),
    /** First page in flight. */
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = false,
    val loadMoreFailed: Boolean = false,
    /** First page failed. */
    val error: ErrorUIModel? = null,
)
