package com.zc.vndb.feature.browse.presentation.component.browsenovelcard.mapper

import com.zc.vndb.R
import com.zc.vndb.common.domain.model.ReleaseDate
import com.zc.vndb.common.ui.component.ratingbadge.mapper.toRatingLabel
import com.zc.vndb.common.ui.mapper.toLabel
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.browse.domain.model.BrowseNovel
import com.zc.vndb.feature.browse.presentation.component.browsenovelcard.model.BrowseNovelCardUIModel
import kotlinx.collections.immutable.toImmutableList

fun BrowseNovel.toCardUIModel(): BrowseNovelCardUIModel = BrowseNovelCardUIModel(
    id = id,
    title = UiText.StringValue(title),
    originalTitle = originalTitle?.let(UiText::StringValue),
    creditsLabel = developerNames.toCreditsLabel(),
    yearLabel = releaseDate.toYearLabel(),
    ratingLabel = rating?.toRatingLabel(),
    lengthLabel = length?.toLabel(),
    coverUrl = cover?.thumbnailUrl ?: cover?.url,
    isCoverSensitive = cover?.isSensitive ?: false,
    tags = tags.map { UiText.StringValue(it.name) }.toImmutableList(),
)

/** "5pb. / Nitroplus": the first two developers, as on the design. */
private fun List<String>.toCreditsLabel(): UiText? = when (size) {
    0 -> null
    1 -> UiText.StringValue(first())
    else -> UiText.StringResource(R.string.credits_separator, listOf(this[0], this[1]))
}

private fun ReleaseDate.toYearLabel(): UiText = when (this) {
    is ReleaseDate.Full -> UiText.StringValue(date.year.toString())
    is ReleaseDate.Month -> UiText.StringValue(yearMonth.year.toString())
    is ReleaseDate.Year -> UiText.StringValue(year.toString())
    ReleaseDate.Tba -> UiText.StringResource(R.string.release_tba)
}
