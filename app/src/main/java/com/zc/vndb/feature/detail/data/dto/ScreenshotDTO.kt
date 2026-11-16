package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScreenshotDTO(
    @SerialName("url") val url: String?,
    @SerialName("thumbnail") val thumbnail: String?,
    @SerialName("sexual") val sexual: Double?,
    @SerialName("violence") val violence: Double?,
    @SerialName("release") val release: ScreenshotReleaseDTO?,
)
