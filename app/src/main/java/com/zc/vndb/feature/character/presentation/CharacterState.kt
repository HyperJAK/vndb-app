package com.zc.vndb.feature.character.presentation

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.component.error.model.ErrorUIModel
import com.zc.vndb.common.ui.component.spoilerfilter.model.SpoilerLevel
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.character.presentation.component.appearancerow.model.AppearanceRowUIModel
import com.zc.vndb.feature.character.presentation.component.characterhero.model.CharacterHeroUIModel
import com.zc.vndb.feature.character.presentation.component.traitgroups.model.TraitGroupUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CharacterState(
    val characterId: String = "",
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: ErrorUIModel? = null,
    val hero: CharacterHeroUIModel? = null,
    val aliases: ImmutableList<UiText> = persistentListOf(),
    val spoilerLevel: SpoilerLevel = SpoilerLevel.None,
    val description: UiText? = null,
    val isDescriptionExpanded: Boolean = false,
    val traitGroups: ImmutableList<TraitGroupUIModel> = persistentListOf(),
    val hiddenTraitsLabel: UiText? = null,
    val appearances: ImmutableList<AppearanceRowUIModel> = persistentListOf(),
    val hiddenAppearancesLabel: UiText? = null,
)
