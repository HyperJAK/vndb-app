package com.zc.vndb.feature.discover.domain.model

import com.zc.vndb.feature.discover.domain.model.DiscoverSamples.date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.datetime.YearMonth

class ReleaseDateTest {

    @Test
    fun `full dates order chronologically`() {
        assertTrue(date(2024, 9, 30) < date(2024, 10, 1))
        assertTrue(date(2023, 12, 31) < date(2024, 1, 1))
    }

    @Test
    fun `partial dates sort after the full dates they contain`() {
        val october = ReleaseDate.Month(YearMonth(2024, 10))
        assertTrue(date(2024, 10, 31) < october)
        assertTrue(october < date(2024, 11, 1))
        assertTrue(october < ReleaseDate.Year(2024))
        assertTrue(ReleaseDate.Year(2024) < date(2025, 1, 1))
    }

    @Test
    fun `TBA sorts after every date`() {
        assertTrue(ReleaseDate.Year(9999) < ReleaseDate.Tba)
        assertEquals(0, ReleaseDate.Tba.compareTo(ReleaseDate.Tba))
    }
}
