package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StaffCreditDTO(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("role") val role: String?,
    @SerialName("note") val note: String?,
    @SerialName("eid") val eid: Int?,
)
