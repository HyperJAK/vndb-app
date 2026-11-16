package com.zc.vndb.feature.profile.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** `GET /authinfo`. */
@Serializable
data class AuthInfoDTO(
    @SerialName("id") val id: String,
    @SerialName("username") val username: String,
    @SerialName("permissions") val permissions: List<String>?,
)
