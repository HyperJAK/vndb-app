package com.zc.vndb.feature.detail.presentation.component.detailhero.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class DetailHeroUIModel(
    val title: UiText,
    val originalTitle: UiText?,
    val coverUrl: String?,
    val isCoverSensitive: Boolean,
    val ratingLabel: UiText?,
    val developerLabel: UiText?,
    val releasedLabel: UiText,
    val lengthLabel: UiText?,
    val averagePlayLabel: UiText?,
    val ageLabel: UiText? = null,
    val tags: ImmutableList<UiText> = persistentListOf(),
)
