package com.zc.vndb.feature.detail.presentation.component.taggroups.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

@Immutable
data class ContentNoteUIModel(val label: UiText, val tone: ContentNoteTone)
