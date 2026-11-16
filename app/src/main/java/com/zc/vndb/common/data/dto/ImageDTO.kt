package com.zc.vndb.common.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageDTO(
    @SerialName("url") val url: String?,
    @SerialName("thumbnail") val thumbnail: String?,
    @SerialName("sexual") val sexual: Double?,
    @SerialName("violence") val violence: Double?,
)
