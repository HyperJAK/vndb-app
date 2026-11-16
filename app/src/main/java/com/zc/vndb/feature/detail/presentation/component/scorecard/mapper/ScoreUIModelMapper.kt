package com.zc.vndb.feature.detail.presentation.component.scorecard.mapper

import com.zc.vndb.common.ui.component.ratingbadge.mapper.toRatingLabel
import com.zc.vndb.common.ui.resource.PluralResource
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.NovelDetail
import com.zc.vndb.feature.detail.presentation.component.scorecard.model.ScoreUIModel

/** Null when the VN has no rating yet. */
fun NovelDetail.toScoreUIModel(): ScoreUIModel? = rating?.let { bayesian ->
    ScoreUIModel(
        ratingLabel = bayesian.toRatingLabel(),
        votesLabel = UiText.StringPlural(
            PluralResource.detail_votes,
            voteCount,
            listOf("%,d".format(voteCount)),
        ),
        averageLabel = average?.let {
            val score = (it.toRatingLabel() as? UiText.StringValue)?.value.orEmpty()
            UiText.StringResource(StringResource.detail_average, listOf(score))
        },
    )
}
