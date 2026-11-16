package com.zc.vndb.common.ui.component.error.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

/** A user-facing error: what to say and whether a retry can help. */
@Immutable
data class ErrorUIModel(val message: UiText, val isRetryable: Boolean)
