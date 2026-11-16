package com.zc.vndb.feature.character.presentation.component.traitgroups.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.component.spoilerfilter.model.SpoilerLevel
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.character.domain.model.CharacterSamples.trait
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TraitGroupUIModelMapperTest {

    private val traits = listOf(
        trait("Kind", group = "Personality"),
        trait("Long", group = "Hair"),
        trait("Mystery Group Trait", group = "Something new"),
        trait("Red", group = "Hair"),
        trait("Minor", group = "Role", spoiler = 1),
        trait("Liar", group = "Personality", lie = true),
    )

    @Test
    fun `groups follow VNDB's order with unknown groups last`() {
        val groups = traits.toTraitGroups(SpoilerLevel.None)

        assertEquals(
            listOf("HAIR", "PERSONALITY", "SOMETHING NEW"),
            groups.map { (it.title as UiText.StringValue).value },
        )
        assertEquals(
            listOf<UiText>(UiText.StringValue("Long"), UiText.StringValue("Red")),
            groups[0].traits,
        )
    }

    @Test
    fun `spoiler and lie traits are hidden and counted until allowed`() {
        assertEquals(
            UiText.StringPlural(R.plurals.detail_hidden_by_spoiler, 2, listOf(2)),
            traits.toHiddenTraitsLabel(SpoilerLevel.None),
        )
        assertEquals(
            UiText.StringPlural(R.plurals.detail_hidden_by_spoiler, 1, listOf(1)),
            traits.toHiddenTraitsLabel(SpoilerLevel.Minor),
        )
        assertNull(traits.toHiddenTraitsLabel(SpoilerLevel.Major))

        val personality = traits.toTraitGroups(
            SpoilerLevel.Major,
        ).single { (it.title as UiText.StringValue).value == "PERSONALITY" }
        assertEquals(
            UiText.StringResource(R.string.character_trait_lie, listOf("Liar")),
            personality.traits.last(),
        )
    }
}
