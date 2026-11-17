package com.zc.vndb.feature.character.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VoiceStaffDTO(@SerialName("id") val id: String, @SerialName("name") val name: String)
