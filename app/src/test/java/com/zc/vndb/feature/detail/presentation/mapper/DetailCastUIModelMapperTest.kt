package com.zc.vndb.feature.detail.presentation.mapper

import com.zc.vndb.R
import com.zc.vndb.common.domain.model.CharacterRole
import com.zc.vndb.common.ui.component.spoilerfilter.model.SpoilerLevel
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.DetailSamples.character
import com.zc.vndb.feature.detail.domain.model.DetailSamples.staff
import com.zc.vndb.feature.detail.domain.model.NovelCast
import com.zc.vndb.feature.detail.domain.model.StaffCreditRole
import com.zc.vndb.feature.detail.domain.model.StaffEdition
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class DetailCastUIModelMapperTest {

    private val cast = NovelCast(
        characters = listOf(
            character("c1", CharacterRole.Main),
            character("c2", CharacterRole.Primary, spoiler = 2),
            character("c3", CharacterRole.Side),
        ),
        staff = listOf(
            staff("s1", StaffCreditRole.Scenario),
            staff("s2", StaffCreditRole.Scenario, note = "Route B"),
            staff("s3", StaffCreditRole.Music),
            staff("s4", StaffCreditRole.Translator, editionId = 1),
        ),
        editions = listOf(
            StaffEdition(
                id = 1,
                name = "English translation",
                languageCode = "en",
                isOfficial = true,
            ),
        ),
    )

    @Test
    fun `characters are grouped by role and filtered by spoiler level`() {
        val model = cast.toCastUIModel(SpoilerLevel.None)

        assertEquals(listOf("c1"), model.preview.map { it.id })
        assertEquals(
            listOf<UiText>(
                UiText.StringResource(R.string.detail_characters_main),
                UiText.StringResource(R.string.detail_characters_side),
            ),
            model.characterGroups.map { it.title },
        )
        assertNotNull(model.hiddenLabel)
        assertEquals(3, model.characterCount)

        assertNull(cast.toCastUIModel(SpoilerLevel.Major).hiddenLabel)
    }

    @Test
    fun `staff are grouped by edition with one line per role`() {
        val groups = cast.toCastUIModel(SpoilerLevel.None).staffGroups

        assertEquals(
            listOf<UiText>(
                UiText.StringResource(R.string.detail_staff_original),
                UiText.StringValue("English translation"),
            ),
            groups.map { it.title },
        )
        assertEquals(
            listOf<UiText>(
                UiText.StringValue("Staff s1, Staff s2 (Route B)"),
                UiText.StringValue("Staff s3"),
            ),
            groups.first().credits.map { it.value },
        )
    }
}
