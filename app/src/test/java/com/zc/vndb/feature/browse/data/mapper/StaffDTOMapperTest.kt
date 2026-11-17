package com.zc.vndb.feature.browse.data.mapper

import com.zc.vndb.common.data.dto.ExtLinkDTO
import com.zc.vndb.common.domain.model.ExternalLink
import com.zc.vndb.feature.browse.data.dto.StaffAliasDTO
import com.zc.vndb.feature.browse.data.dto.StaffDTO
import com.zc.vndb.feature.browse.domain.model.StaffGender
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StaffDTOMapperTest {

    private fun dto(
        name: String = "Nasu Kinoko",
        original: String? = "奈須 きのこ",
        gender: String? = "m",
        aliases: List<StaffAliasDTO>? = null,
        description: String? = null,
        links: List<ExtLinkDTO>? = null,
    ) = StaffDTO(
        id = "s2",
        name = name,
        original = original,
        lang = "ja",
        gender = gender,
        aliases = aliases,
        description = description,
        extlinks = links,
    )

    @Test
    fun `maps a full staff record`() {
        val staff = dto(
            aliases = listOf(
                StaffAliasDTO(name = "奈須 きのこ", latin = "Nasu Kinoko"),
                StaffAliasDTO(name = "奈須 國広", latin = "Nasu Kunihiro"),
                StaffAliasDTO(name = "那須 邦宏", latin = "Nasu Kunihiro"),
            ),
            description = "Writer.",
            links = listOf(ExtLinkDTO("Wikipedia (en)", "https://en.wikipedia.org/wiki/Nasu")),
        ).toDomain()

        assertEquals("s2", staff.id)
        assertEquals("奈須 きのこ", staff.originalName)
        assertEquals(listOf("Nasu Kunihiro"), staff.aliases)
        assertEquals("ja", staff.language)
        assertEquals(StaffGender.Male, staff.gender)
        assertEquals("Writer.", staff.description)
        assertEquals(
            listOf(ExternalLink("Wikipedia (en)", "https://en.wikipedia.org/wiki/Nasu")),
            staff.links,
        )
    }

    @Test
    fun `aliases without a romanisation show as written, minus the main names`() {
        val staff = dto(
            aliases = listOf(
                StaffAliasDTO(name = "奈須 きのこ", latin = null),
                StaffAliasDTO(name = "Kinoko", latin = null),
            ),
        ).toDomain()

        assertEquals(listOf("Kinoko"), staff.aliases)
    }

    @Test
    fun `maps genders and drops blank or duplicate fields`() {
        assertEquals(StaffGender.Female, dto(gender = "f").toDomain().gender)
        assertNull(dto(gender = null).toDomain().gender)
        assertNull(dto(gender = "x").toDomain().gender)

        val bare = dto(name = "Key", original = "Key", description = " ").toDomain()
        assertNull(bare.originalName)
        assertNull(bare.description)
        assertEquals(emptyList(), bare.aliases)
        assertEquals(emptyList(), bare.links)
    }
}
