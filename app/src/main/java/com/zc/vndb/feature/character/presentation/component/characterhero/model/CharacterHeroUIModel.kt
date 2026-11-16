package com.zc.vndb.feature.character.presentation.component.characterhero.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CharacterHeroUIModel(
    val name: UiText,
    val originalName: UiText?,
    val imageUrl: String?,
    val isImageSensitive: Boolean,
    /** Sex and gender chips. */
    val identity: ImmutableList<UiText> = persistentListOf(),
    /** Spoiler sex / gender, shown with a warning tint. */
    val spoilerIdentity: ImmutableList<UiText> = persistentListOf(),
    val facts: ImmutableList<CharacterFactUIModel> = persistentListOf(),
)
