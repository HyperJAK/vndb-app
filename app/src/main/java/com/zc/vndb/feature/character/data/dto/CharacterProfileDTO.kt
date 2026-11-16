package com.zc.vndb.feature.character.data.dto

import com.zc.vndb.common.data.dto.ImageDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterProfileDTO(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("original") val original: String?,
    @SerialName("aliases") val aliases: List<String>?,
    @SerialName("description") val description: String?,
    @SerialName("image") val image: ImageDTO?,
    @SerialName("age") val age: Int?,
    /** [month, day]. */
    @SerialName("birthday") val birthday: List<Int>?,
    @SerialName("blood_type") val bloodType: String?,
    @SerialName("height") val height: Int?,
    @SerialName("weight") val weight: Int?,
    @SerialName("bust") val bust: Int?,
    @SerialName("waist") val waist: Int?,
    @SerialName("hips") val hips: Int?,
    @SerialName("cup") val cup: String?,
    /** [apparent, spoiler]; either entry may be null. */
    @SerialName("sex") val sex: List<String?>?,
    /** [apparent, spoiler]; either entry may be null. */
    @SerialName("gender") val gender: List<String?>?,
    @SerialName("traits") val traits: List<CharacterTraitDTO>?,
    @SerialName("vns") val vns: List<CharacterAppearanceDTO>?,
)
