package com.zc.vndb.feature.login.presentation

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

@Immutable
data class LoginState(
    val token: String = "",
    val isTokenVisible: Boolean = false,
    /** Checking the token with VNDB. */
    val isSubmitting: Boolean = false,
    /** Why the last attempt failed, shown under the field. */
    val error: UiText? = null,
) {
    val canSubmit: Boolean get() = token.isNotBlank() && !isSubmitting
}
