package com.zc.vndb.feature.detail.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.component.factline.model.FactUIModel
import com.zc.vndb.common.ui.text.UiText
import kotlinx.collections.immutable.ImmutableList

/** Staff credits for one edition (the original, or e.g. a translation). */
@Immutable
data class StaffGroupUIModel(val title: UiText, val credits: ImmutableList<FactUIModel>)
