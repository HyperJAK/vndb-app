package com.zc.vndb.feature.character.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterVoiceCreditDTO(
    @SerialName("character") val character: CharacterRefDTO,
    @SerialName("staff") val staff: VoiceStaffDTO,
)
