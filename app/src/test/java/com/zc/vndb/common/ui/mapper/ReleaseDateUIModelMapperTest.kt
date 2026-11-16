package com.zc.vndb.common.ui.mapper

import com.zc.vndb.common.domain.model.ReleaseDate
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth

class ReleaseDateUIModelMapperTest {

    @Test
    fun `iso labels as VNDB writes them`() {
        assertEquals(
            UiText.StringValue("2009-10-15"),
            ReleaseDate.Full(LocalDate(2009, 10, 15)).toIsoLabel(),
        )
        assertEquals(
            UiText.StringValue("2021-03"),
            ReleaseDate.Month(YearMonth(2021, 3)).toIsoLabel(),
        )
        assertEquals(UiText.StringValue("2021"), ReleaseDate.Year(2021).toIsoLabel())
        assertEquals(
            UiText.StringResource(StringResource.release_tba),
            ReleaseDate.Tba.toIsoLabel(),
        )
    }

    @Test
    fun `year labels`() {
        assertEquals(
            UiText.StringValue("2009"),
            ReleaseDate.Full(LocalDate(2009, 10, 15)).toYearLabel(),
        )
        assertEquals(
            UiText.StringValue("2021"),
            ReleaseDate.Month(YearMonth(2021, 3)).toYearLabel(),
        )
        assertEquals(UiText.StringValue("2020"), ReleaseDate.Year(2020).toYearLabel())
        assertEquals(
            UiText.StringResource(StringResource.release_tba),
            ReleaseDate.Tba.toYearLabel(),
        )
    }
}
