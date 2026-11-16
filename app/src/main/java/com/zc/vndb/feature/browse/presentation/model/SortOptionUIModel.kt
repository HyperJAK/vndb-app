package com.zc.vndb.feature.browse.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.browse.domain.model.BrowseSort

@Immutable
data class SortOptionUIModel(val sort: BrowseSort, val label: UiText)
