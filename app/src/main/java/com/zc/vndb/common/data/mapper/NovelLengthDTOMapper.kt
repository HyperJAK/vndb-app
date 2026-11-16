package com.zc.vndb.common.data.mapper

import com.zc.vndb.common.domain.model.NovelLength

// Upper bounds (exclusive, in minutes) of VNDB's length ranges.
private const val VERY_SHORT_MAX_MINUTES = 2 * 60
private const val SHORT_MAX_MINUTES = 10 * 60
private const val MEDIUM_MAX_MINUTES = 30 * 60
private const val LONG_MAX_MINUTES = 50 * 60

/**
 * VNDB play time: the vote-based [lengthMinutes] when present, otherwise the 1–5 [estimate]
 * (`length` field), otherwise unknown.
 */
fun toNovelLength(lengthMinutes: Int?, estimate: Int?): NovelLength? =
    lengthMinutes?.toLengthRange() ?: estimate?.let { NovelLength.entries.getOrNull(it - 1) }

private fun Int.toLengthRange(): NovelLength = when {
    this < VERY_SHORT_MAX_MINUTES -> NovelLength.VeryShort
    this < SHORT_MAX_MINUTES -> NovelLength.Short
    this < MEDIUM_MAX_MINUTES -> NovelLength.Medium
    this < LONG_MAX_MINUTES -> NovelLength.Long
    else -> NovelLength.VeryLong
}
