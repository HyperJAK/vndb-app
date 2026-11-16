package com.zc.vndb.feature.detail.presentation

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.component.error.model.ErrorUIModel
import com.zc.vndb.common.ui.component.spoilerfilter.model.SpoilerLevel
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.presentation.component.charactercard.model.CharacterCardUIModel
import com.zc.vndb.feature.detail.presentation.component.detailhero.model.DetailHeroUIModel
import com.zc.vndb.feature.detail.presentation.component.editionrow.model.EditionRowUIModel
import com.zc.vndb.feature.detail.presentation.component.linksrow.model.LinkUIModel
import com.zc.vndb.feature.detail.presentation.component.relationcard.model.RelationCardUIModel
import com.zc.vndb.feature.detail.presentation.component.scorecard.model.ScoreUIModel
import com.zc.vndb.feature.detail.presentation.component.taggroups.model.ContentNoteUIModel
import com.zc.vndb.feature.detail.presentation.component.taggroups.model.TagGroupUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class DetailState(
    val novelId: String = "",
    val isLoading: Boolean = false,
    val error: ErrorUIModel? = null,
    val hero: DetailHeroUIModel? = null,
    val score: ScoreUIModel? = null,
    val links: ImmutableList<LinkUIModel> = persistentListOf(),
    val spoilerLevel: SpoilerLevel = SpoilerLevel.None,
    val synopsis: UiText? = null,
    val isSynopsisExpanded: Boolean = false,
    val tagGroups: ImmutableList<TagGroupUIModel> = persistentListOf(),
    val hiddenTagsLabel: UiText? = null,
    val contentNotes: ImmutableList<ContentNoteUIModel> = persistentListOf(),
    val characters: ImmutableList<CharacterCardUIModel> = persistentListOf(),
    val mainCharactersLabel: UiText? = null,
    val hiddenCharactersLabel: UiText? = null,
    val editions: ImmutableList<EditionRowUIModel> = persistentListOf(),
    val relations: ImmutableList<RelationCardUIModel> = persistentListOf(),
)
