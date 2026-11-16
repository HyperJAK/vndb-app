package com.zc.vndb.feature.detail.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.presentation.component.charactercard.model.CharacterCardUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/** Characters and staff after the spoiler filter. */
@Immutable
data class CastUIModel(
    /** Main and primary characters, for the Overview preview. */
    val preview: ImmutableList<CharacterCardUIModel> = persistentListOf(),
    val mainCountLabel: UiText? = null,
    val characterGroups: ImmutableList<CharacterGroupUIModel> = persistentListOf(),
    val staffGroups: ImmutableList<StaffGroupUIModel> = persistentListOf(),
    val hiddenLabel: UiText? = null,
    val characterCount: Int = 0,
)
