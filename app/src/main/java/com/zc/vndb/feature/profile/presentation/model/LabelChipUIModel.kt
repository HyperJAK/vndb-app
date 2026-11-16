package com.zc.vndb.feature.profile.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

/** A list category, e.g. "Playing (3)". */
@Immutable
data class LabelChipUIModel(val id: Int, val label: UiText, val isSelected: Boolean)
