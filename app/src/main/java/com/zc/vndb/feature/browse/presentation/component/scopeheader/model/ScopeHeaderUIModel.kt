package com.zc.vndb.feature.browse.presentation.component.scopeheader.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

/** What a tag or producer page is about, shown above the filters. */
@Immutable
data class ScopeHeaderUIModel(
    val name: UiText,
    /** The name in its original script, when it differs. */
    val originalName: UiText?,
    /** Tag category or producer type. */
    val typeLabel: UiText?,
    val isSexualCategory: Boolean,
    /** VNDB's own count, when the endpoint has one (tags do, producers do not). */
    val countLabel: UiText?,
    val aliasesLabel: UiText?,
    val aboutTitle: UiText,
    val description: UiText?,
)
