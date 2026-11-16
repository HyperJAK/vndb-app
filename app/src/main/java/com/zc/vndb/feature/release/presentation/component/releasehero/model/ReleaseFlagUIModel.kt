package com.zc.vndb.feature.release.presentation.component.releasehero.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

@Immutable
data class ReleaseFlagUIModel(val label: UiText, val tone: ReleaseFlagTone)
