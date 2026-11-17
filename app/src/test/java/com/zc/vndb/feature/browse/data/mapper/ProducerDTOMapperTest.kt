package com.zc.vndb.feature.browse.data.mapper

import com.zc.vndb.feature.browse.data.dto.ProducerDTO
import com.zc.vndb.feature.browse.domain.model.ProducerType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProducerDTOMapperTest {

    @Test
    fun `maps a full producer`() {
        val producer = ProducerDTO(
            id = "p30",
            name = "Nitroplus",
            original = "ニトロプラス",
            aliases = listOf("Nitro+", " "),
            lang = "ja",
            type = "co",
            description = "A studio.",
        ).toDomain()

        assertEquals("p30", producer.id)
        assertEquals("ニトロプラス", producer.originalName)
        assertEquals(listOf("Nitro+"), producer.aliases)
        assertEquals(ProducerType.Company, producer.type)
        assertEquals("ja", producer.language)
        assertEquals("A studio.", producer.description)
    }

    @Test
    fun `maps the other producer types`() {
        fun type(code: String?) =
            ProducerDTO("p1", "X", null, null, null, code, null).toDomain().type

        assertEquals(ProducerType.Individual, type("in"))
        assertEquals(ProducerType.AmateurGroup, type("ng"))
        assertNull(type("zz"))
        assertNull(type(null))
    }

    @Test
    fun `drops an original name equal to the name and blank fields`() {
        val producer = ProducerDTO("p1", "Key", "Key", null, " ", null, " ").toDomain()

        assertNull(producer.originalName)
        assertNull(producer.language)
        assertNull(producer.description)
        assertEquals(emptyList(), producer.aliases)
    }
}
