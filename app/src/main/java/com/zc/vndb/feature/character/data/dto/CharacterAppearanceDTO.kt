package com.zc.vndb.feature.character.data.dto

import com.zc.vndb.common.data.dto.ImageDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** One entry of a character's `vns`: the VN fields plus the character's role in it. */
@Serializable
data class CharacterAppearanceDTO(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String?,
    @SerialName("released") val released: String?,
    @SerialName("rating") val rating: Double?,
    @SerialName("image") val image: ImageDTO?,
    @SerialName("role") val role: String?,
    @SerialName("spoiler") val spoiler: Int?,
    @SerialName("alttitle") val altTitle: String? = null,
)
