package com.zc.vndb.feature.discover.data.mapper

import com.zc.vndb.feature.discover.domain.model.ReleaseDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth

private const val YEAR_ONLY_LENGTH = 4
private const val YEAR_MONTH_LENGTH = 7

/**
 * Parses VNDB's release date strings: "2024-10-05", "2024-10", "2024" or "TBA".
 * Anything missing or unparseable is treated as [ReleaseDate.Tba].
 */
fun String?.toReleaseDate(): ReleaseDate {
    val value = this?.trim().orEmpty()
    return when (value.length) {
        YEAR_ONLY_LENGTH -> value.toIntOrNull()?.let { ReleaseDate.Year(it) }
        YEAR_MONTH_LENGTH -> runCatchingParse { ReleaseDate.Month(YearMonth.parse(value)) }
        else -> runCatchingParse { ReleaseDate.Full(LocalDate.parse(value)) }
    } ?: ReleaseDate.Tba
}

private inline fun runCatchingParse(parse: () -> ReleaseDate): ReleaseDate? = try {
    parse()
} catch (_: IllegalArgumentException) {
    null
}
