package com.zc.vndb.common.ui.component.entrylabel

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

/** A label on a list entry; [id] is VNDB's label id and picks the look. */
@Immutable
data class EntryLabelUIModel(val id: Int, val name: UiText)
