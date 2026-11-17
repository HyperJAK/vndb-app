package com.zc.vndb.feature.theme.presentation

import com.zc.vndb.common.domain.model.AppTheme

sealed interface ThemeIntent {
    data object BackClicked : ThemeIntent

    /** Selecting a theme applies and saves it at once. */
    data class ThemeSelected(val theme: AppTheme) : ThemeIntent
}
