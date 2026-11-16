package com.zc.vndb.feature.detail.presentation.component.mylist.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/** The signed-in user's entry for this VN, under the score. */
@Immutable
data class MyListCardUIModel(
    val isOnList: Boolean,
    /** The token has `listwrite`. */
    val canEdit: Boolean,
    /** Status first, then the other labels. */
    val labels: ImmutableList<UiText> = persistentListOf(),
    /** "My vote 8.5". */
    val voteLabel: UiText? = null,
    /** "Started 2024-10-15", "Finished 2024-11-02". */
    val dates: ImmutableList<UiText> = persistentListOf(),
    val notes: UiText? = null,
)
