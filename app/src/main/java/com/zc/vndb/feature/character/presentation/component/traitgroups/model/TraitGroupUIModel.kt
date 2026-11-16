package com.zc.vndb.feature.character.presentation.component.traitgroups.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class TraitGroupUIModel(val title: UiText, val traits: ImmutableList<UiText>)
