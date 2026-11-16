package com.zc.vndb.feature.browse.data.mapper

import com.zc.vndb.common.data.mapper.toCover
import com.zc.vndb.common.data.mapper.toDomain
import com.zc.vndb.common.data.mapper.toReleaseDate
import com.zc.vndb.feature.browse.data.dto.BrowseNovelDTO
import com.zc.vndb.feature.browse.data.dto.BrowseNovelPageDTO
import com.zc.vndb.feature.browse.domain.model.BrowseNovel
import com.zc.vndb.feature.browse.domain.model.BrowsePage
import com.zc.vndb.feature.browse.domain.model.NovelLength

// Upper bounds (exclusive, in minutes) of VNDB's length ranges.
private const val VERY_SHORT_MAX_MINUTES = 2 * 60
private const val SHORT_MAX_MINUTES = 10 * 60
private const val MEDIUM_MAX_MINUTES = 30 * 60
private const val LONG_MAX_MINUTES = 50 * 60

fun BrowseNovelPageDTO.toDomain(): BrowsePage = BrowsePage(
    novels = results.map { it.toDomain() },
    totalCount = count,
    hasMore = more,
)

fun BrowseNovelDTO.toDomain(): BrowseNovel = BrowseNovel(
    id = id,
    title = title.orEmpty(),
    originalTitle = alttitle?.takeIf { it.isNotBlank() && it != title },
    releaseDate = released.toReleaseDate(),
    rating = rating,
    cover = image?.toCover(),
    developerNames = developers.orEmpty().map { it.name },
    tags = tags.orEmpty().mapNotNull { it.toDomain() },
    length = lengthMinutes?.toNovelLength() ?: length.toEstimatedLength(),
)

/** Vote-based play time in minutes → range. */
private fun Int.toNovelLength(): NovelLength = when {
    this < VERY_SHORT_MAX_MINUTES -> NovelLength.VeryShort
    this < SHORT_MAX_MINUTES -> NovelLength.Short
    this < MEDIUM_MAX_MINUTES -> NovelLength.Medium
    this < LONG_MAX_MINUTES -> NovelLength.Long
    else -> NovelLength.VeryLong
}

/** VNDB's 1–5 length estimate, in the same order as [NovelLength]. */
private fun Int?.toEstimatedLength(): NovelLength? = this?.let {
    NovelLength.entries.getOrNull(
        it - 1,
    )
}
