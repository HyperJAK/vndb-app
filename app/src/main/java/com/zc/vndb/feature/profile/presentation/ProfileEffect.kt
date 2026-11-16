package com.zc.vndb.feature.profile.presentation

import com.zc.vndb.common.ui.text.UiText

sealed interface ProfileEffect {
    data object NavigateToLogin : ProfileEffect
    data object NavigateToSettings : ProfileEffect
    data class NavigateToNovel(val id: String) : ProfileEffect
    data class ShowMessage(val message: UiText) : ProfileEffect
}
