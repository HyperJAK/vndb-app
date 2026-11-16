package com.zc.vndb.feature.discover.presentation.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.discover.domain.model.DatabaseStats
import kotlin.test.Test
import kotlin.test.assertEquals

class RatingUIModelMapperTest {

    @Test
    fun `ratings show out of ten with two decimals`() {
        assertEquals(UiText.StringValue("8.92"), 89.2.toRatingLabel())
        assertEquals(UiText.StringValue("9.00"), 90.0.toRatingLabel())
        assertEquals(UiText.StringValue("9.08"), 90.75.toRatingLabel())
        assertEquals(UiText.StringValue("1.05"), 10.5.toRatingLabel())
        assertEquals(UiText.StringValue("10.00"), 100.0.toRatingLabel())
    }

    @Test
    fun `featured rating uses the out-of-ten resource`() {
        assertEquals(
            UiText.StringResource(R.string.rating_out_of_ten, listOf("8.92")),
            89.2.toRatingOutOfTenLabel(),
        )
    }

    @Test
    fun `stats count is a grouped plural`() {
        assertEquals(
            UiText.StringPlural(
                R.plurals.discover_stats_novels,
                68053,
                listOf("%,d".format(68053)),
            ),
            DatabaseStats(novelCount = 68053).toCountLabel(),
        )
    }
}
