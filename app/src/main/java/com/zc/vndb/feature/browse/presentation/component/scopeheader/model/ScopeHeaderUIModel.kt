package com.zc.vndb.feature.browse.presentation.component.scopeheader.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.component.linksrow.model.LinkUIModel
import com.zc.vndb.common.ui.text.UiText
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

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
    /** The page on vndb.org and external sites, when the entity has them. */
    val links: ImmutableList<LinkUIModel> = persistentListOf(),
)
