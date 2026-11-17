package com.zc.vndb.feature.discover.data.mapper

import com.zc.vndb.feature.discover.data.dto.QuoteCharacterDTO
import com.zc.vndb.feature.discover.data.dto.QuoteDTO
import com.zc.vndb.feature.discover.data.dto.QuoteNovelDTO
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class QuoteDTOMapperTest {

    private fun dto(
        altTitle: String? = "うみねこのなく頃に",
        character: QuoteCharacterDTO? = QuoteCharacterDTO("Ushiromiya Battler"),
    ) = QuoteDTO(
        id = "q1867",
        quote = "  It's useless, it's all useless!\n",
        vn = QuoteNovelDTO(id = "v24", title = "Umineko no Naku Koro ni", altTitle = altTitle),
        character = character,
    )

    @Test
    fun `maps the quote, its novel and its character`() {
        val quote = dto().toDomain()

        assertEquals("q1867", quote.id)
        assertEquals("It's useless, it's all useless!", quote.text)
        assertEquals("v24", quote.novelId)
        assertEquals("Umineko no Naku Koro ni", quote.novelTitle)
        assertEquals("うみねこのなく頃に", quote.novelOriginalTitle)
        assertEquals("Ushiromiya Battler", quote.characterName)
    }

    @Test
    fun `a quote with no character or alternative title keeps those null`() {
        val quote = dto(altTitle = " ", character = null).toDomain()

        assertNull(quote.characterName)
        assertNull(quote.novelOriginalTitle)
        assertNull(dto(character = QuoteCharacterDTO(null)).toDomain().characterName)
    }

    @Test
    fun `an alternative title equal to the title is dropped`() {
        assertNull(dto(altTitle = "Umineko no Naku Koro ni").toDomain().novelOriginalTitle)
    }
}
