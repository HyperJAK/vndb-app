package com.zc.vndb.feature.character.presentation.component.characterhero.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.component.factline.model.FactUIModel
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
    /** Distinct voice actors across the character's VNs; each opens their staff page. */
    val voiceActors: ImmutableList<HeroVoiceActorUIModel> = persistentListOf(),
    val facts: ImmutableList<FactUIModel> = persistentListOf(),
)

@Immutable
data class HeroVoiceActorUIModel(val id: String, val name: String)
