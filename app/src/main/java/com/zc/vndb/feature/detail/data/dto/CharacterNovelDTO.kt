package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A character's appearance in one VN. */
@Serializable
data class CharacterNovelDTO(
    @SerialName("id") val id: String,
    @SerialName("role") val role: String?,
    @SerialName("spoiler") val spoiler: Int?,
)
