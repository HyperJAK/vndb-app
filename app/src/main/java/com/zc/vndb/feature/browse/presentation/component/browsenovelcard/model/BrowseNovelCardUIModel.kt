package com.zc.vndb.feature.browse.presentation.component.browsenovelcard.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class BrowseNovelCardUIModel(
    val id: String,
    val title: UiText,
    val creditsLabel: UiText?,
    val yearLabel: UiText,
    val ratingLabel: UiText?,
    val lengthLabel: UiText?,
    val coverUrl: String?,
    val isCoverSensitive: Boolean,
    val tags: ImmutableList<UiText> = persistentListOf(),
)
