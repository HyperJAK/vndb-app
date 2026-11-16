package com.zc.vndb.feature.profile.presentation

sealed interface LoginEffect {
    data class OpenUrl(val url: String) : LoginEffect
    data object NavigateBack : LoginEffect

    /** The token was accepted and stored. */
    data object LoggedIn : LoginEffect
}
