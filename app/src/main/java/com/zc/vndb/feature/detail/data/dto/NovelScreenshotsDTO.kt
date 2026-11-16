package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** The VN record's screenshots only. */
@Serializable
data class NovelScreenshotsDTO(
    @SerialName("id") val id: String,
    @SerialName("screenshots") val screenshots: List<ScreenshotDTO>?,
)
