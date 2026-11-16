package com.zc.vndb.feature.release.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

@Immutable
data class ReleaseLanguageUIModel(
    val code: String,
    val name: UiText,
    val title: UiText?,
    val isMachineTranslated: Boolean,
)
