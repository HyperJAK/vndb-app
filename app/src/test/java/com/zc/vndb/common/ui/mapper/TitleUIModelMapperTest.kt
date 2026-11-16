package com.zc.vndb.common.ui.mapper

import com.zc.vndb.common.domain.model.TitleLanguage
import com.zc.vndb.common.ui.text.UiText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TitleUIModelMapperTest {

    @Test
    fun `romanized puts the main title first`() {
        assertEquals(
            UiText.StringValue("Steins;Gate"),
            primaryTitle("Steins;Gate", "シュタインズ・ゲート", TitleLanguage.Romanized),
        )
        assertEquals(
            UiText.StringValue("シュタインズ・ゲート"),
            secondaryTitle("Steins;Gate", "シュタインズ・ゲート", TitleLanguage.Romanized),
        )
    }

    @Test
    fun `original script puts the original title first`() {
        assertEquals(
            UiText.StringValue("シュタインズ・ゲート"),
            primaryTitle("Steins;Gate", "シュタインズ・ゲート", TitleLanguage.Original),
        )
        assertEquals(
            UiText.StringValue("Steins;Gate"),
            secondaryTitle("Steins;Gate", "シュタインズ・ゲート", TitleLanguage.Original),
        )
    }

    @Test
    fun `without a distinct original only the main title shows`() {
        listOf(null, "", "Clannad").forEach { original ->
            assertEquals(
                UiText.StringValue("Clannad"),
                primaryTitle("Clannad", original, TitleLanguage.Original),
            )
            assertNull(secondaryTitle("Clannad", original, TitleLanguage.Original))
        }
    }
}
