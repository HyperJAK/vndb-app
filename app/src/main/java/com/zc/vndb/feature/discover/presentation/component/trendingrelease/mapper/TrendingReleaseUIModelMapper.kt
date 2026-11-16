package com.zc.vndb.feature.discover.presentation.component.trendingrelease.mapper

import com.zc.vndb.common.domain.model.ReleaseDate
import com.zc.vndb.common.domain.model.TitleLanguage
import com.zc.vndb.common.ui.component.ratingbadge.mapper.toRatingLabel
import com.zc.vndb.common.ui.mapper.platformLabel
import com.zc.vndb.common.ui.mapper.primaryTitle
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.discover.domain.model.Platform
import com.zc.vndb.feature.discover.domain.model.ReleaseKind
import com.zc.vndb.feature.discover.domain.model.TrendingRelease
import com.zc.vndb.feature.discover.presentation.component.trendingrelease.model.ReleaseKindTone
import com.zc.vndb.feature.discover.presentation.component.trendingrelease.model.TrendingReleaseUIModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.YearMonth
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.yearMonth

/** Rows have room for this many platform chips. */
private const val MAX_PLATFORMS = 2

private val monthYearFormat = YearMonth.Format {
    monthName(MonthNames.ENGLISH_ABBREVIATED)
    char(' ')
    year()
}

fun TrendingRelease.toUIModel(
    titleLanguage: TitleLanguage = TitleLanguage.Romanized,
): TrendingReleaseUIModel = TrendingReleaseUIModel(
    releaseId = releaseId,
    novelId = novelId,
    title = primaryTitle(title, originalTitle, titleLanguage),
    kindLabel = kind.toLabel(),
    kindTone = kind.toTone(),
    dateLabel = releaseDate.toLabel(),
    ratingLabel = rating?.toRatingLabel(),
    thumbnailUrl = cover?.thumbnailUrl ?: cover?.url,
    isCoverSensitive = cover?.isSensitive ?: false,
    platforms = platforms.take(MAX_PLATFORMS).map { it.toLabel() }.toImmutableList(),
)

/** "Oct 2024" for full and month dates, "2024" for a year, "TBA" otherwise. */
fun ReleaseDate.toLabel(): UiText = when (this) {
    is ReleaseDate.Full -> UiText.StringValue(monthYearFormat.format(date.yearMonth))
    is ReleaseDate.Month -> UiText.StringValue(monthYearFormat.format(yearMonth))
    is ReleaseDate.Year -> UiText.StringValue(year.toString())
    ReleaseDate.Tba -> UiText.StringResource(StringResource.release_tba)
}

private fun Platform.toLabel(): UiText = platformLabel(code)

private fun ReleaseKind.toLabel(): UiText = UiText.StringResource(
    when (this) {
        ReleaseKind.NewRelease -> StringResource.discover_release_new
        ReleaseKind.NewTranslation -> StringResource.discover_release_translation
        ReleaseKind.RecentPatch -> StringResource.discover_release_patch
        ReleaseKind.ReRelease -> StringResource.discover_release_rerelease
    },
)

private fun ReleaseKind.toTone(): ReleaseKindTone = when (this) {
    ReleaseKind.NewRelease -> ReleaseKindTone.New
    ReleaseKind.NewTranslation -> ReleaseKindTone.Translation
    ReleaseKind.RecentPatch -> ReleaseKindTone.Patch
    ReleaseKind.ReRelease -> ReleaseKindTone.Rerelease
}
