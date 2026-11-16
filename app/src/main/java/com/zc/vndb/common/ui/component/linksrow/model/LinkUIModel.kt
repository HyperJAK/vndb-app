package com.zc.vndb.common.ui.component.linksrow.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

@Immutable
data class LinkUIModel(
    val label: UiText,
    val url: String,
    /** The vndb.org page itself (shown with a different icon). */
    val isVndb: Boolean,
)
