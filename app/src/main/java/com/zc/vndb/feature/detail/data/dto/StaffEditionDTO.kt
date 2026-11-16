package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StaffEditionDTO(
    @SerialName("eid") val eid: Int,
    @SerialName("lang") val lang: String?,
    @SerialName("name") val name: String,
    @SerialName("official") val official: Boolean?,
)
