package com.zc.vndb.common.domain.model

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CoverTest {

    private fun cover(sexual: Double, violence: Double) =
        Cover(url = "u", thumbnailUrl = null, sexual = sexual, violence = violence)

    @Test
    fun `safe and mildly flagged covers are not sensitive`() {
        assertFalse(cover(sexual = 0.0, violence = 0.0).isSensitive)
        assertFalse(cover(sexual = 0.99, violence = 0.99).isSensitive)
    }

    @Test
    fun `suggestive or violent covers are sensitive`() {
        assertTrue(cover(sexual = 1.0, violence = 0.0).isSensitive)
        assertTrue(cover(sexual = 0.0, violence = 1.5).isSensitive)
    }
}
