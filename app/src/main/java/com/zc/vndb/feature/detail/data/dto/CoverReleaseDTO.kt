package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A release with only its images. */
@Serializable
data class CoverReleaseDTO(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String?,
    @SerialName("images") val images: List<CoverImageDTO>?,
)
