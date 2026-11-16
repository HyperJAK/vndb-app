package com.zc.vndb.feature.detail.presentation.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.Quote
import com.zc.vndb.feature.detail.presentation.model.QuoteUIModel

fun Quote.toUIModel(): QuoteUIModel = QuoteUIModel(
    id = id,
    text = UiText.StringValue(text),
    characterId = characterId,
    characterName = characterName?.let(UiText::StringValue),
    scoreLabel = UiText.StringResource(R.string.detail_quote_score, listOf(score)),
)

/** The VN's reviews on vndb.org (the API has no reviews endpoint). */
fun reviewsUrl(novelId: String): String = "https://vndb.org/$novelId/reviews"
