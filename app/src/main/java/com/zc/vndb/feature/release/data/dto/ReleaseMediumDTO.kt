package com.zc.vndb.feature.release.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseMediumDTO(
    @SerialName("medium") val medium: String,
    @SerialName("qty") val qty: Int?,
)
