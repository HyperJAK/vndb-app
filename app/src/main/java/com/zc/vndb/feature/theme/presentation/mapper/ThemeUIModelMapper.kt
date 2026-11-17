package com.zc.vndb.feature.theme.presentation.mapper

import com.zc.vndb.common.domain.model.AppTheme
import com.zc.vndb.common.ui.mapper.toLabel
import com.zc.vndb.common.ui.mapper.toSpec
import com.zc.vndb.feature.theme.presentation.ThemeState
import com.zc.vndb.feature.theme.presentation.component.themepreview.model.ThemeUIModel
import kotlinx.collections.immutable.toImmutableList

/** Every theme, in display order, with [selected] marked. */
fun themeState(selected: AppTheme): ThemeState = ThemeState(
    themes = AppTheme.entries.map {
        ThemeUIModel(
            theme = it,
            name = it.toLabel(),
            spec = it.toSpec(),
            isSelected = it == selected,
        )
    }.toImmutableList(),
)
