package com.zc.vndb.feature.theme.presentation.component.themepreview.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.domain.model.AppTheme
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbThemeSpec

/** One theme on the Theme screen: its name, the [spec] its preview is painted with, and whether it is in use. */
@Immutable
data class ThemeUIModel(
    val theme: AppTheme,
    val name: UiText,
    val spec: VndbThemeSpec,
    val isSelected: Boolean,
)
