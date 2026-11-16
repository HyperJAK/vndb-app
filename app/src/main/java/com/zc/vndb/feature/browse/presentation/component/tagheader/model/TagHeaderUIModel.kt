package com.zc.vndb.feature.browse.presentation.component.tagheader.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

@Immutable
data class TagHeaderUIModel(
    val name: UiText,
    val categoryLabel: UiText?,
    val isSexualCategory: Boolean,
    val countLabel: UiText,
    val aliasesLabel: UiText?,
    val description: UiText?,
)
