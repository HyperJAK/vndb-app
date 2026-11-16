package com.zc.vndb.feature.browse.presentation.component.browsenovelcard.mapper

import com.zc.vndb.R
import com.zc.vndb.common.domain.model.ReleaseDate
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.browse.domain.model.BrowseSamples
import com.zc.vndb.feature.browse.domain.model.NovelLength
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.datetime.YearMonth

class BrowseNovelCardUIModelMapperTest {

    private val novel = BrowseSamples.novel(
        "v2002",
        tags = listOf(BrowseSamples.tag("Time Travel")),
    )

    @Test
    fun `maps display values`() {
        val card = novel.copy(originalTitle = "シュタインズ・ゲート").toCardUIModel()

        assertEquals(UiText.StringValue("Novel v2002"), card.title)
        assertEquals(UiText.StringValue("シュタインズ・ゲート"), card.originalTitle)
        assertEquals(UiText.StringValue("MAGES."), card.creditsLabel)
        assertEquals(UiText.StringValue("2009"), card.yearLabel)
        assertEquals(UiText.StringValue("9.02"), card.ratingLabel)
        assertEquals(UiText.StringResource(R.string.length_long), card.lengthLabel)
        assertEquals("thumb", card.coverUrl)
        assertEquals(listOf<UiText>(UiText.StringValue("Time Travel")), card.tags)
    }

    @Test
    fun `credits show the first two developers`() {
        val card = novel.copy(developerNames = listOf("5pb.", "Nitroplus", "Third")).toCardUIModel()

        assertEquals(
            UiText.StringResource(R.string.credits_separator, listOf("5pb.", "Nitroplus")),
            card.creditsLabel,
        )
        assertNull(novel.copy(developerNames = emptyList()).toCardUIModel().creditsLabel)
    }

    @Test
    fun `year comes from partial dates, TBA otherwise`() {
        assertEquals(
            UiText.StringValue("2021"),
            novel.copy(
                releaseDate = ReleaseDate.Month(YearMonth(2021, 3)),
            ).toCardUIModel().yearLabel,
        )
        assertEquals(
            UiText.StringValue("2020"),
            novel.copy(releaseDate = ReleaseDate.Year(2020)).toCardUIModel().yearLabel,
        )
        assertEquals(
            UiText.StringResource(R.string.release_tba),
            novel.copy(releaseDate = ReleaseDate.Tba).toCardUIModel().yearLabel,
        )
    }

    @Test
    fun `every length has a label and missing values stay null`() {
        val labels = NovelLength.entries.map { novel.copy(length = it).toCardUIModel().lengthLabel }
        assertEquals(
            listOf(
                R.string.length_very_short,
                R.string.length_short,
                R.string.length_medium,
                R.string.length_long,
                R.string.length_very_long,
            ).map { UiText.StringResource(it) },
            labels,
        )
        val bare = novel.copy(length = null, rating = null, cover = null).toCardUIModel()
        assertNull(bare.lengthLabel)
        assertNull(bare.ratingLabel)
        assertNull(bare.coverUrl)
    }
}
