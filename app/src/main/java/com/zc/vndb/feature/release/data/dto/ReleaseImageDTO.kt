package com.zc.vndb.feature.release.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseImageDTO(
    @SerialName("type") val type: String?,
    @SerialName("url") val url: String?,
    @SerialName("thumbnail") val thumbnail: String?,
    @SerialName("sexual") val sexual: Double?,
    @SerialName("violence") val violence: Double?,
)
