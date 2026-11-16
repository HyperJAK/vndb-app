package com.zc.vndb.feature.character.domain.model

import com.zc.vndb.common.domain.model.Cover

/** Everything the character page shows. */
data class CharacterProfile(
    val id: String,
    val name: String,
    val originalName: String?,
    val aliases: List<String>,
    /** VNDB BBCode. */
    val description: String?,
    val image: Cover?,
    val age: Int?,
    val birthday: Birthday?,
    /** "a", "b", "ab" or "o". */
    val bloodType: String?,
    val heightCm: Int?,
    val weightKg: Int?,
    val measurements: Measurements?,
    val sex: Sex?,
    /** The real sex when it is a spoiler that differs from [sex]. */
    val spoilerSex: Sex?,
    val gender: Gender?,
    /** The real gender when it is a spoiler that differs from [gender]. */
    val spoilerGender: Gender?,
    val traits: List<CharacterTrait>,
    val appearances: List<CharacterAppearance>,
)
