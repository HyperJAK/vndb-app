package com.zc.vndb.feature.detail.presentation.component.relationcard.mapper

import com.zc.vndb.common.domain.model.TitleLanguage
import com.zc.vndb.common.ui.component.ratingbadge.mapper.toRatingLabel
import com.zc.vndb.common.ui.mapper.primaryTitle
import com.zc.vndb.common.ui.mapper.toYearLabel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.RelatedNovel
import com.zc.vndb.feature.detail.domain.model.RelationType
import com.zc.vndb.feature.detail.presentation.component.relationcard.model.RelationCardUIModel

fun RelatedNovel.toCardUIModel(
    titleLanguage: TitleLanguage = TitleLanguage.Romanized,
): RelationCardUIModel = RelationCardUIModel(
    id = id,
    title = primaryTitle(title, originalTitle, titleLanguage),
    relationLabel = relation.toLabel(),
    isOfficial = isOfficial,
    yearLabel = releaseDate.toYearLabel(),
    scoreLabel = rating?.let {
        val score = (it.toRatingLabel() as? UiText.StringValue)?.value.orEmpty()
        UiText.StringResource(StringResource.detail_relation_score, listOf(score))
    },
)

private fun RelationType.toLabel(): UiText = UiText.StringResource(
    when (this) {
        RelationType.Sequel -> StringResource.detail_relation_sequel
        RelationType.Prequel -> StringResource.detail_relation_prequel
        RelationType.SameSetting -> StringResource.detail_relation_same_setting
        RelationType.AlternativeVersion -> StringResource.detail_relation_alternative
        RelationType.SharesCharacters -> StringResource.detail_relation_shares_characters
        RelationType.SideStory -> StringResource.detail_relation_side_story
        RelationType.ParentStory -> StringResource.detail_relation_parent_story
        RelationType.SameSeries -> StringResource.detail_relation_same_series
        RelationType.FanDisc -> StringResource.detail_relation_fan_disc
        RelationType.OriginalGame -> StringResource.detail_relation_original
        RelationType.Other -> StringResource.detail_relation_other
    },
)
