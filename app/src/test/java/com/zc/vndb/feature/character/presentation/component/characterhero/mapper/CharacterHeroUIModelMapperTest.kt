package com.zc.vndb.feature.character.presentation.component.characterhero.mapper

import com.zc.vndb.common.domain.model.CharacterRole
import com.zc.vndb.common.domain.model.VoiceActor
import com.zc.vndb.common.ui.component.spoilerfilter.model.SpoilerLevel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.character.domain.model.CharacterSamples.appearance
import com.zc.vndb.feature.character.domain.model.CharacterSamples.profile
import com.zc.vndb.feature.character.domain.model.Gender
import com.zc.vndb.feature.character.domain.model.Measurements
import com.zc.vndb.feature.character.domain.model.Sex
import com.zc.vndb.feature.character.presentation.component.characterhero.model.HeroVoiceActorUIModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CharacterHeroUIModelMapperTest {

    @Test
    fun `facts list only what VNDB knows, formatted`() {
        val facts = profile().toHeroUIModel(SpoilerLevel.None).facts

        assertEquals(
            listOf(
                StringResource.character_age,
                StringResource.character_birthday,
                StringResource.character_blood_type,
                StringResource.character_height,
                StringResource.character_weight,
                StringResource.character_measurements,
            ),
            facts.map { (it.label as UiText.StringResource).id },
        )
        assertEquals(UiText.StringValue("Jul 25"), facts[1].value)
        assertEquals(UiText.StringValue("A"), facts[2].value)
        assertEquals(
            UiText.StringResource(StringResource.character_height_value, listOf(160)),
            facts[3].value,
        )
        assertEquals(
            UiText.StringResource(
                StringResource.character_measurements_value,
                listOf("79", "56", "83"),
            ),
            facts[5].value,
        )

        val withCup = profile().copy(
            measurements = Measurements(bust = 79, waist = null, hips = null, cup = "C"),
        )
            .toHeroUIModel(SpoilerLevel.None).facts.last().value
        assertEquals(
            UiText.StringResource(
                StringResource.character_measurements_with_cup,
                listOf("79", "?", "?", "C"),
            ),
            withCup,
        )
    }

    @Test
    fun `gender shows only when it differs from sex`() {
        assertEquals(
            listOf<UiText>(UiText.StringResource(StringResource.character_sex_female)),
            profile().toHeroUIModel(SpoilerLevel.None).identity,
        )
        assertEquals(
            listOf<UiText>(
                UiText.StringResource(StringResource.character_sex_male),
                UiText.StringResource(StringResource.character_gender_female),
            ),
            profile(
                sex = Sex.Male,
                gender = Gender.Female,
            ).toHeroUIModel(SpoilerLevel.None).identity,
        )
    }

    @Test
    fun `the real sex and gender only show at Major`() {
        val twist = profile(spoilerSex = Sex.Male, spoilerGender = Gender.NonBinary)

        assertTrue(twist.toHeroUIModel(SpoilerLevel.Minor).spoilerIdentity.isEmpty())
        assertEquals(
            listOf<UiText>(
                UiText.StringResource(StringResource.character_sex_actually_male),
                UiText.StringResource(StringResource.character_gender_non_binary),
            ),
            twist.toHeroUIModel(SpoilerLevel.Major).spoilerIdentity,
        )
    }

    @Test
    fun `voice actors are distinct across VNs, in order of first appearance`() {
        val withActors = profile().copy(
            appearances = listOf(
                appearance("v1", CharacterRole.Primary)
                    .copy(voiceActor = VoiceActor("s240", "Imai Asami")),
                appearance("v2", CharacterRole.Appears).copy(voiceActor = null),
                appearance("v3", CharacterRole.Appears)
                    .copy(voiceActor = VoiceActor("s240", "Imai Asami")),
                appearance("v4", CharacterRole.Appears)
                    .copy(voiceActor = VoiceActor("s7", "Other Actor")),
            ),
        )

        assertEquals(
            listOf(
                HeroVoiceActorUIModel("s240", "Imai Asami"),
                HeroVoiceActorUIModel("s7", "Other Actor"),
            ),
            withActors.toHeroUIModel(SpoilerLevel.None).voiceActors,
        )
        assertTrue(profile().toHeroUIModel(SpoilerLevel.None).voiceActors.isEmpty())
    }
}
