package com.zc.vndb.feature.browse.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.browse.domain.model.Genre

@Immutable
data class GenreChipUIState(val genre: Genre, val label: UiText, val isSelected: Boolean)
