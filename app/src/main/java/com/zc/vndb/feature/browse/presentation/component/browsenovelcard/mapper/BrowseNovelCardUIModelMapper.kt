package com.zc.vndb.feature.browse.presentation.component.browsenovelcard.mapper

import com.zc.vndb.common.domain.model.TitleLanguage
import com.zc.vndb.common.ui.component.ratingbadge.mapper.toRatingLabel
import com.zc.vndb.common.ui.mapper.primaryTitle
import com.zc.vndb.common.ui.mapper.secondaryTitle
import com.zc.vndb.common.ui.mapper.toNameWithRangeLabel
import com.zc.vndb.common.ui.mapper.toYearLabel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.browse.domain.model.BrowseNovel
import com.zc.vndb.feature.browse.presentation.component.browsenovelcard.model.BrowseNovelCardUIModel
import kotlinx.collections.immutable.toImmutableList

fun BrowseNovel.toCardUIModel(
    titleLanguage: TitleLanguage = TitleLanguage.Romanized,
): BrowseNovelCardUIModel = toCardUIModel(
    primaryTitle(title, originalTitle, titleLanguage),
    secondaryTitle(title, originalTitle, titleLanguage),
)

private fun BrowseNovel.toCardUIModel(primaryTitle: UiText, secondaryTitle: UiText?) =
    BrowseNovelCardUIModel(
        id = id,
        title = primaryTitle,
        originalTitle = secondaryTitle,
        creditsLabel = developerNames.toCreditsLabel(),
        yearLabel = releaseDate.toYearLabel(),
        ratingLabel = rating?.toRatingLabel(),
        lengthLabel = length?.toNameWithRangeLabel(),
        coverUrl = cover?.thumbnailUrl ?: cover?.url,
        isCoverSensitive = cover?.isSensitive ?: false,
        tags = tags.map { UiText.StringValue(it.name) }.toImmutableList(),
    )

/** "5pb. / Nitroplus": the first two developers, as on the design. */
private fun List<String>.toCreditsLabel(): UiText? = when (size) {
    0 -> null
    1 -> UiText.StringValue(first())
    else -> UiText.StringResource(StringResource.credits_separator, listOf(this[0], this[1]))
}
