package com.zc.vndb.common.data.mapper

import com.zc.vndb.common.domain.model.ReleaseDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth

class ReleaseDateDTOMapperTest {

    @Test
    fun `parses full, year-month and year dates`() {
        assertEquals(ReleaseDate.Full(LocalDate(2024, 10, 5)), "2024-10-05".toReleaseDate())
        assertEquals(ReleaseDate.Month(YearMonth(2024, 10)), "2024-10".toReleaseDate())
        assertEquals(ReleaseDate.Year(2024), "2024".toReleaseDate())
    }

    @Test
    fun `TBA, null and garbage become Tba`() {
        listOf("TBA", null, "", "soon", "2024-13", "2024-02-30").forEach { raw ->
            assertEquals(ReleaseDate.Tba, raw.toReleaseDate(), "for $raw")
        }
    }
}
