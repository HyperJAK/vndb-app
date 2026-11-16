package com.zc.vndb.feature.character.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A VN with its voice-actor credits. */
@Serializable
data class CharacterNovelVoicesDTO(
    @SerialName("id") val id: String,
    @SerialName("va") val va: List<CharacterVoiceCreditDTO>?,
)
