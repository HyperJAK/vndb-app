package com.zc.vndb.feature.detail.presentation.component.detailhero.mapper

import com.zc.vndb.R
import com.zc.vndb.common.domain.model.ReleaseDate
import com.zc.vndb.common.domain.model.TagCategory
import com.zc.vndb.common.ui.component.ratingbadge.mapper.toRatingLabel
import com.zc.vndb.common.ui.mapper.toLabel
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.AgeRange
import com.zc.vndb.feature.detail.domain.model.NovelDetail
import com.zc.vndb.feature.detail.presentation.component.detailhero.model.DetailHeroUIModel
import kotlin.math.roundToInt
import kotlinx.collections.immutable.toImmutableList

private const val HERO_TAGS = 2
private const val MAX_DEVELOPERS = 2
private const val MINUTES_PER_HOUR = 60.0
private const val ALL_AGES = 0

fun NovelDetail.toHeroUIModel(ageRange: AgeRange?): DetailHeroUIModel = DetailHeroUIModel(
    title = UiText.StringValue(title),
    originalTitle = originalTitle?.let(UiText::StringValue),
    coverUrl = cover?.url,
    isCoverSensitive = cover?.isSensitive ?: false,
    ratingLabel = rating?.toRatingLabel(),
    developerLabel = developerNames.takeIf { it.isNotEmpty() }
        ?.let { UiText.StringValue(it.take(MAX_DEVELOPERS).joinToString(" / ")) },
    releasedLabel = releaseDate.toIsoLabel(),
    lengthLabel = length?.toLabel(),
    averagePlayLabel = averagePlayMinutes?.let {
        UiText.StringResource(
            R.string.detail_average_hours,
            listOf((it / MINUTES_PER_HOUR).roundToInt().toString()),
        )
    },
    ageLabel = ageRange?.toLabel(),
    tags = tags
        .filter { it.category == TagCategory.Content && it.spoilerLevel == 0 }
        .sortedByDescending { it.rating }
        .take(HERO_TAGS)
        .map { UiText.StringValue(it.name) }
        .toImmutableList(),
)

/** "2009-10-15", "2009-10", "2009" or TBA, as VNDB writes them. */
fun ReleaseDate.toIsoLabel(): UiText = when (this) {
    is ReleaseDate.Full -> UiText.StringValue(date.toString())
    is ReleaseDate.Month -> UiText.StringValue(yearMonth.toString())
    is ReleaseDate.Year -> UiText.StringValue(year.toString())
    ReleaseDate.Tba -> UiText.StringResource(R.string.release_tba)
}

/** "All Ages", "18+", "All Ages / 15+" or "12+ / 18+". */
fun AgeRange.toLabel(): UiText = when {
    max == ALL_AGES -> UiText.StringResource(R.string.detail_age_all)
    min == max -> UiText.StringResource(R.string.detail_age_min, listOf(max))
    min == ALL_AGES -> UiText.StringResource(R.string.detail_age_range_from_all, listOf(max))
    else -> UiText.StringResource(R.string.detail_age_range, listOf(min, max))
}
