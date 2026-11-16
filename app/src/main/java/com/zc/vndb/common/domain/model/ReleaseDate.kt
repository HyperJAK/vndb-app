package com.zc.vndb.common.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.number

/**
 * A VNDB release date, which may be partial ("2024", "2024-10") or unknown ("TBA").
 * Ordering treats missing parts as the end of their period and [Tba] as later than any date.
 */
sealed interface ReleaseDate : Comparable<ReleaseDate> {
    data class Full(val date: LocalDate) : ReleaseDate
    data class Month(val yearMonth: YearMonth) : ReleaseDate
    data class Year(val year: Int) : ReleaseDate
    data object Tba : ReleaseDate

    override fun compareTo(other: ReleaseDate): Int = sortKey.compareTo(other.sortKey)
}

private const val UNKNOWN_PART = 99
private const val YEAR_FACTOR = 10_000
private const val MONTH_FACTOR = 100

private val ReleaseDate.sortKey: Long
    get() = when (this) {
        is ReleaseDate.Full -> key(date.year, date.month.number, date.day)
        is ReleaseDate.Month -> key(yearMonth.year, yearMonth.month.number, UNKNOWN_PART)
        is ReleaseDate.Year -> key(year, UNKNOWN_PART, UNKNOWN_PART)
        ReleaseDate.Tba -> Long.MAX_VALUE
    }

private fun key(year: Int, month: Int, day: Int): Long =
    year.toLong() * YEAR_FACTOR + month * MONTH_FACTOR + day
