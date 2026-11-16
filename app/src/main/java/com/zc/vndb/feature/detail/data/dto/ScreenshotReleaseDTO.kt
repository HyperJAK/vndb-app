package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScreenshotReleaseDTO(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String?,
)
