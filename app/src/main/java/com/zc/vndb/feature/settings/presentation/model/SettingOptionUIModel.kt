package com.zc.vndb.feature.settings.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

/** One choice in a single- or multi-select dialog; [key] identifies it in intents. */
@Immutable
data class SettingOptionUIModel(val key: String, val label: UiText, val isSelected: Boolean)
