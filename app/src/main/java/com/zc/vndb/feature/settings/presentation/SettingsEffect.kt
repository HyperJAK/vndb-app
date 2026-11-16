package com.zc.vndb.feature.settings.presentation

import com.zc.vndb.common.ui.text.UiText

sealed interface SettingsEffect {
    data object NavigateBack : SettingsEffect
    data class OpenUrl(val url: String) : SettingsEffect
    data class ShowMessage(val message: UiText) : SettingsEffect
}
