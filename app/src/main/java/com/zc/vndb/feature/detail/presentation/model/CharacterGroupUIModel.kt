package com.zc.vndb.feature.detail.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.presentation.component.charactercard.model.CharacterCardUIModel
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class CharacterGroupUIModel(
    val title: UiText,
    val characters: ImmutableList<CharacterCardUIModel>,
)
