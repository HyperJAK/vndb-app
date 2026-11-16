package com.zc.vndb.common.ui.component.ratingbadge.mapper

import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
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
            UiText.StringResource(StringResource.rating_out_of_ten, listOf("8.92")),
            89.2.toRatingOutOfTenLabel(),
        )
    }
}
