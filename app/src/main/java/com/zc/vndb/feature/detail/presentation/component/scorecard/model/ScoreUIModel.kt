package com.zc.vndb.feature.detail.presentation.component.scorecard.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

@Immutable
data class ScoreUIModel(val ratingLabel: UiText, val votesLabel: UiText, val averageLabel: UiText?)
