package com.zc.vndb.feature.discover.presentation.component.trendingrelease.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class TrendingReleaseUIModel(
    val releaseId: String,
    val novelId: String,
    val title: UiText,
    val kindLabel: UiText,
    val kindTone: ReleaseKindTone,
    val dateLabel: UiText,
    val ratingLabel: UiText?,
    val thumbnailUrl: String?,
    val isCoverSensitive: Boolean,
    val platforms: ImmutableList<UiText> = persistentListOf(),
)
