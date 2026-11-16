package com.zc.vndb.feature.character.presentation.component.characterhero.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.component.spoilerfilter.model.SpoilerLevel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.character.domain.model.Birthday
import com.zc.vndb.feature.character.domain.model.CharacterProfile
import com.zc.vndb.feature.character.domain.model.Gender
import com.zc.vndb.feature.character.domain.model.Measurements
import com.zc.vndb.feature.character.domain.model.Sex
import com.zc.vndb.feature.character.presentation.component.characterhero.model.CharacterFactUIModel
import com.zc.vndb.feature.character.presentation.component.characterhero.model.CharacterHeroUIModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Month
import kotlinx.datetime.format.MonthNames

private const val UNKNOWN = "?"

/** The real sex / gender is a spoiler, so it only shows at [SpoilerLevel.Major]. */
fun CharacterProfile.toHeroUIModel(level: SpoilerLevel): CharacterHeroUIModel =
    CharacterHeroUIModel(
        name = UiText.StringValue(name),
        originalName = originalName?.let(UiText::StringValue),
        imageUrl = image?.url,
        isImageSensitive = image?.isSensitive ?: false,
        identity = listOfNotNull(
            sex?.let { UiText.StringResource(it.labelRes()) },
            gender?.takeUnless { it.matches(sex) }?.let { UiText.StringResource(it.labelRes()) },
        ).toImmutableList(),
        spoilerIdentity = if (level == SpoilerLevel.Major) {
            listOfNotNull(
                spoilerSex?.let { UiText.StringResource(it.actuallyRes()) },
                spoilerGender?.let { UiText.StringResource(it.labelRes()) },
            ).toImmutableList()
        } else {
            persistentListOf()
        },
        facts = facts().toImmutableList(),
    )

private fun CharacterProfile.facts(): List<CharacterFactUIModel> = listOfNotNull(
    age?.let { fact(R.string.character_age, UiText.StringValue(it.toString())) },
    birthday?.let { fact(R.string.character_birthday, UiText.StringValue(it.format())) },
    bloodType?.let { fact(R.string.character_blood_type, UiText.StringValue(it.uppercase())) },
    heightCm?.let {
        fact(
            R.string.character_height,
            UiText.StringResource(R.string.character_height_value, listOf(it)),
        )
    },
    weightKg?.let {
        fact(
            R.string.character_weight,
            UiText.StringResource(R.string.character_weight_value, listOf(it)),
        )
    },
    measurements?.let { fact(R.string.character_measurements, it.toLabel()) },
)

private fun fact(label: StringResource, value: UiText) = CharacterFactUIModel(
    label = label,
    value = value,
)

/** "Jul 25". */
private fun Birthday.format(): String {
    val monthName = Month.entries.getOrNull(
        month - 1,
    )?.let { MonthNames.ENGLISH_ABBREVIATED.names[it.ordinal] }
    return listOfNotNull(monthName, day.toString()).joinToString(" ")
}

/** "B79 W56 H83 (C)", with "?" for unknown parts. */
private fun Measurements.toLabel(): UiText {
    val parts = listOf(bust, waist, hips).map { it?.toString() ?: UNKNOWN }
    return if (cup == null) {
        UiText.StringResource(R.string.character_measurements_value, parts)
    } else {
        UiText.StringResource(R.string.character_measurements_with_cup, parts + cup)
    }
}

private fun Gender.matches(sex: Sex?): Boolean =
    (this == Gender.Male && sex == Sex.Male) || (this == Gender.Female && sex == Sex.Female)

private fun Sex.labelRes(): StringResource = when (this) {
    Sex.Male -> R.string.character_sex_male
    Sex.Female -> R.string.character_sex_female
    Sex.Both -> R.string.character_sex_both
    Sex.Sexless -> R.string.character_sex_sexless
}

private fun Sex.actuallyRes(): StringResource = when (this) {
    Sex.Male -> R.string.character_sex_actually_male
    Sex.Female -> R.string.character_sex_actually_female
    Sex.Both -> R.string.character_sex_actually_both
    Sex.Sexless -> R.string.character_sex_actually_sexless
}

private fun Gender.labelRes(): StringResource = when (this) {
    Gender.Male -> R.string.character_gender_male
    Gender.Female -> R.string.character_gender_female
    Gender.NonBinary -> R.string.character_gender_non_binary
    Gender.Ambiguous -> R.string.character_gender_ambiguous
}
