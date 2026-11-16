package com.zc.vndb.feature.profile.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.domain.model.ListSort
import com.zc.vndb.common.ui.text.UiText

/** One entry of the list sort menu. */
@Immutable
data class SortOptionUIModel(val sort: ListSort, val label: UiText, val isSelected: Boolean)
