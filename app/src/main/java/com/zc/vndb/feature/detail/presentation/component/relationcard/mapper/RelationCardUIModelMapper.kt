package com.zc.vndb.feature.detail.presentation.component.relationcard.mapper

import com.zc.vndb.R
import com.zc.vndb.common.domain.model.ReleaseDate
import com.zc.vndb.common.ui.component.ratingbadge.mapper.toRatingLabel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.RelatedNovel
import com.zc.vndb.feature.detail.domain.model.RelationType
import com.zc.vndb.feature.detail.presentation.component.relationcard.model.RelationCardUIModel

fun RelatedNovel.toCardUIModel(): RelationCardUIModel = RelationCardUIModel(
    id = id,
    title = UiText.StringValue(title),
    relationLabel = UiText.StringResource(relation.labelRes()),
    isOfficial = isOfficial,
    yearLabel = when (val date = releaseDate) {
        is ReleaseDate.Full -> UiText.StringValue(date.date.year.toString())
        is ReleaseDate.Month -> UiText.StringValue(date.yearMonth.year.toString())
        is ReleaseDate.Year -> UiText.StringValue(date.year.toString())
        ReleaseDate.Tba -> UiText.StringResource(R.string.release_tba)
    },
    scoreLabel = rating?.let {
        val score = (it.toRatingLabel() as? UiText.StringValue)?.value.orEmpty()
        UiText.StringResource(R.string.detail_relation_score, listOf(score))
    },
)

private fun RelationType.labelRes(): StringResource = when (this) {
    RelationType.Sequel -> R.string.detail_relation_sequel
    RelationType.Prequel -> R.string.detail_relation_prequel
    RelationType.SameSetting -> R.string.detail_relation_same_setting
    RelationType.AlternativeVersion -> R.string.detail_relation_alternative
    RelationType.SharesCharacters -> R.string.detail_relation_shares_characters
    RelationType.SideStory -> R.string.detail_relation_side_story
    RelationType.ParentStory -> R.string.detail_relation_parent_story
    RelationType.SameSeries -> R.string.detail_relation_same_series
    RelationType.FanDisc -> R.string.detail_relation_fan_disc
    RelationType.OriginalGame -> R.string.detail_relation_original
    RelationType.Other -> R.string.detail_relation_other
}
