package com.zc.vndb.feature.detail.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

/** "Japanese · ハロー・レディ！", with the romanised title below when there is one. */
@Immutable
data class TitleLineUIModel(val language: UiText, val title: UiText, val latin: UiText?)
