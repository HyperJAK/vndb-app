package com.zc.vndb.feature.login.presentation

sealed interface LoginIntent {
    data class TokenChanged(val token: String) : LoginIntent
    data object TokenVisibilityToggled : LoginIntent
    data object OpenTokensPageClicked : LoginIntent
    data object SubmitClicked : LoginIntent
    data object ContinueAsGuestClicked : LoginIntent
    data object BackClicked : LoginIntent
}
