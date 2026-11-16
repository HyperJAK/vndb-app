package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VoiceActorDTO(
    @SerialName("character") val character: VoiceActorCharacterDTO,
    @SerialName("staff") val staff: VoiceActorStaffDTO,
)
