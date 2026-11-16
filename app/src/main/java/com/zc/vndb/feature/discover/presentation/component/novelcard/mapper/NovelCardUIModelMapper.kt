package com.zc.vndb.feature.discover.presentation.component.novelcard.mapper

import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.discover.domain.model.DiscoverNovel
import com.zc.vndb.feature.discover.presentation.component.novelcard.model.NovelCardUIModel
import com.zc.vndb.feature.discover.presentation.mapper.toRatingLabel

/** Cards are small, so the thumbnail is preferred over the full cover. */
fun DiscoverNovel.toNovelCardUIModel(): NovelCardUIModel = NovelCardUIModel(
    id = id,
    title = UiText.StringValue(title),
    subtitle = developerName?.let(UiText::StringValue),
    ratingLabel = rating?.toRatingLabel(),
    coverUrl = cover?.thumbnailUrl ?: cover?.url,
    isCoverSensitive = cover?.isSensitive ?: false,
)
