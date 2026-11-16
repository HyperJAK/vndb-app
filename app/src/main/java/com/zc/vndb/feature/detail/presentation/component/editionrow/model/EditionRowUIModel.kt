package com.zc.vndb.feature.detail.presentation.component.editionrow.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.PlatformFamily

@Immutable
data class EditionRowUIModel(
    val key: String,
    val title: UiText,
    val platformLabel: UiText,
    val languagesLabel: UiText?,
    val dateLabel: UiText,
    val family: PlatformFamily,
    val familyLabel: UiText,
)
