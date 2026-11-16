package com.zc.vndb.feature.character.presentation.component.appearancerow.mapper

import com.zc.vndb.R
import com.zc.vndb.common.domain.model.CharacterRole
import com.zc.vndb.common.domain.model.ReleaseDate
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.character.domain.model.CharacterSamples.appearance
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AppearanceRowUIModelMapperTest {

    @Test
    fun `maps role, year, score and voice actor`() {
        val row = appearance(
            "v2002",
            CharacterRole.Main,
        ).copy(voiceActor = "Imai Asami").toRowUIModel()

        assertEquals(UiText.StringResource(R.string.character_role_main), row.roleLabel)
        assertTrue(row.isMajorRole)
        assertEquals(UiText.StringValue("2009"), row.yearLabel)
        assertEquals(UiText.StringValue("9.02"), row.ratingLabel)
        assertEquals(
            UiText.StringResource(R.string.detail_cv, listOf("Imai Asami")),
            row.voiceActorLabel,
        )
    }

    @Test
    fun `minor roles, unknown dates and missing values`() {
        val row = appearance(
            "v1",
            CharacterRole.Appears,
        ).copy(releaseDate = ReleaseDate.Tba, rating = null).toRowUIModel()

        assertFalse(row.isMajorRole)
        assertEquals(UiText.StringResource(R.string.character_role_appears), row.roleLabel)
        assertEquals(UiText.StringResource(R.string.release_tba), row.yearLabel)
        assertNull(row.ratingLabel)
        assertNull(row.voiceActorLabel)
        assertNull(row.thumbnailUrl)
    }
}
