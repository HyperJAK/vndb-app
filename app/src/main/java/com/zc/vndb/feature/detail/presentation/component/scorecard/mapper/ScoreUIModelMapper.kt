package com.zc.vndb.feature.detail.presentation.component.scorecard.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.component.ratingbadge.mapper.toRatingLabel
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.NovelDetail
import com.zc.vndb.feature.detail.presentation.component.scorecard.model.ScoreUIModel

/** Null when the VN has no rating yet. */
fun NovelDetail.toScoreUIModel(): ScoreUIModel? = rating?.let { bayesian ->
    ScoreUIModel(
        ratingLabel = bayesian.toRatingLabel(),
        votesLabel = UiText.StringPlural(
            R.plurals.detail_votes,
            voteCount,
            listOf("%,d".format(voteCount)),
        ),
        averageLabel = average?.let {
            val score = (it.toRatingLabel() as? UiText.StringValue)?.value.orEmpty()
            UiText.StringResource(R.string.detail_average, listOf(score))
        },
    )
}
