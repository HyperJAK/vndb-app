package com.zc.vndb.feature.release.presentation.component.releasehero.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.component.factline.model.FactUIModel
import com.zc.vndb.common.ui.text.UiText
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ReleaseHeroUIModel(
    val title: UiText,
    val altTitle: UiText?,
    val imageUrl: String?,
    val isImageSensitive: Boolean,
    val flags: ImmutableList<ReleaseFlagUIModel> = persistentListOf(),
    val facts: ImmutableList<FactUIModel> = persistentListOf(),
)
