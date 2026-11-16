package com.zc.vndb.feature.detail.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class AlsoKnownAsUIModel(
    val titles: ImmutableList<TitleLineUIModel>,
    val aliases: ImmutableList<UiText>,
)
