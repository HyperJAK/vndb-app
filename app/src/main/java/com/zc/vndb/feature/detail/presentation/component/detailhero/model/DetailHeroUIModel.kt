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
    /** Length category, shown as a pill: "Very Long". */
    val lengthName: UiText?,
    /** Average play time from votes, else the category's range: "(73h)", "(>50h)". */
    val lengthValueLabel: UiText?,
    /** "90 votes", when the play time comes from votes. */
    val lengthVotesLabel: UiText? = null,
    val ageLabel: UiText? = null,
    val tags: ImmutableList<UiText> = persistentListOf(),
)
