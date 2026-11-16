package com.zc.vndb.feature.discover.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VisualNovelDTO(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String?,
    @SerialName("rating") val rating: Double?,
    @SerialName("description") val description: String?,
    @SerialName("image") val image: ImageDTO?,
    @SerialName("developers") val developers: List<DeveloperDTO>?,
    @SerialName("staff") val staff: List<StaffDTO>?,
    @SerialName("tags") val tags: List<TagDTO>?,
)
