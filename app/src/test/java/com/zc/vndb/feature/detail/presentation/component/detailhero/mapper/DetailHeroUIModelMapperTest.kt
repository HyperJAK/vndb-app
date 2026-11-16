package com.zc.vndb.feature.detail.presentation.component.detailhero.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.AgeRange
import com.zc.vndb.feature.detail.domain.model.DetailSamples.novel
import com.zc.vndb.feature.detail.domain.model.DetailSamples.tag
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DetailHeroUIModelMapperTest {

    @Test
    fun `maps display values`() {
        val hero = novel(
            tags = listOf(
                tag("A", 1.0),
                tag("B", 3.0),
                tag("C", 2.0),
                tag("Spoiler", 3.0, spoiler = 1),
            ),
        )
            .toHeroUIModel(AgeRange(0, 15))

        assertEquals(UiText.StringValue("9.02"), hero.ratingLabel)
        assertEquals(UiText.StringValue("NITRO PLUS / MAGES."), hero.developerLabel)
        assertEquals(UiText.StringValue("2009-10-15"), hero.releasedLabel)
        assertEquals(UiText.StringResource(R.string.length_long), hero.lengthLabel)
        assertEquals(
            UiText.StringResource(R.string.detail_average_hours, listOf("44")),
            hero.averagePlayLabel,
        )
        assertEquals(listOf<UiText>(UiText.StringValue("B"), UiText.StringValue("C")), hero.tags)
        assertEquals(
            UiText.StringResource(R.string.detail_age_range_from_all, listOf(15)),
            hero.ageLabel,
        )
    }

    @Test
    fun `age labels`() {
        assertEquals(UiText.StringResource(R.string.detail_age_all), AgeRange(0, 0).toLabel())
        assertEquals(
            UiText.StringResource(R.string.detail_age_min, listOf(18)),
            AgeRange(18, 18).toLabel(),
        )
        assertEquals(
            UiText.StringResource(R.string.detail_age_range, listOf(12, 18)),
            AgeRange(12, 18).toLabel(),
        )
        assertNull(novel().toHeroUIModel(ageRange = null).ageLabel)
    }
}
