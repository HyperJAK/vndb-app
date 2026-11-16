package com.zc.vndb.feature.discover.presentation.component.trendingrelease.mapper

import com.zc.vndb.R
import com.zc.vndb.common.domain.model.ReleaseDate
import com.zc.vndb.common.ui.component.ratingbadge.mapper.toRatingLabel
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

private val platformNames = mapOf(
    "win" to "Windows",
    "lin" to "Linux",
    "mac" to "macOS",
    "web" to "Web",
    "and" to "Android",
    "ios" to "iOS",
    "swi" to "Switch",
    "ps3" to "PS3",
    "ps4" to "PS4",
    "ps5" to "PS5",
    "psv" to "PS Vita",
    "psp" to "PSP",
    "n3d" to "3DS",
    "xb1" to "Xbox One",
    "xxs" to "Xbox Series",
    "dvd" to "DVD",
)

fun TrendingRelease.toUIModel(): TrendingReleaseUIModel = TrendingReleaseUIModel(
    releaseId = releaseId,
    novelId = novelId,
    title = UiText.StringValue(title),
    kindLabel = UiText.StringResource(kind.labelRes()),
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
    ReleaseDate.Tba -> UiText.StringResource(R.string.release_tba)
}

private fun Platform.toLabel(): UiText = UiText.StringValue(platformNames[code] ?: code.uppercase())

private fun ReleaseKind.labelRes(): StringResource = when (this) {
    ReleaseKind.NewRelease -> R.string.discover_release_new
    ReleaseKind.NewTranslation -> R.string.discover_release_translation
    ReleaseKind.RecentPatch -> R.string.discover_release_patch
    ReleaseKind.ReRelease -> R.string.discover_release_rerelease
}

private fun ReleaseKind.toTone(): ReleaseKindTone = when (this) {
    ReleaseKind.NewRelease -> ReleaseKindTone.New
    ReleaseKind.NewTranslation -> ReleaseKindTone.Translation
    ReleaseKind.RecentPatch -> ReleaseKindTone.Patch
    ReleaseKind.ReRelease -> ReleaseKindTone.Rerelease
}
