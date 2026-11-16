package com.zc.vndb.feature.browse.data.mapper

import com.zc.vndb.common.domain.model.TagCategory
import com.zc.vndb.feature.browse.data.dto.TagInfoDTO
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TagInfoDTOMapperTest {

    @Test
    fun `maps the tag and tolerates missing fields`() {
        val tag = TagInfoDTO(
            "g32",
            "ADV",
            listOf("AVG", " "),
            "Text box.",
            "tech",
            36379,
        ).toDomain()

        assertEquals(listOf("AVG"), tag.aliases)
        assertEquals(TagCategory.Technical, tag.category)
        assertEquals(36379, tag.novelCount)

        val bare = TagInfoDTO("g1", "New", null, " ", "new-category", null).toDomain()
        assertNull(bare.category)
        assertNull(bare.description)
        assertEquals(0, bare.novelCount)
    }
}
