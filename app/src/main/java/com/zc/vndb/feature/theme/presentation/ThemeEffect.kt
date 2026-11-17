package com.zc.vndb.feature.theme.presentation

sealed interface ThemeEffect {
    data object NavigateBack : ThemeEffect
}
