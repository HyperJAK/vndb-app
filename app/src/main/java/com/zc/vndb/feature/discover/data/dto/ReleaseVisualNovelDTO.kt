package com.zc.vndb.feature.discover.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseVisualNovelDTO(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String?,
    @SerialName("olang") val olang: String?,
    @SerialName("released") val released: String?,
    @SerialName("rating") val rating: Double?,
    @SerialName("image") val image: ImageDTO?,
)
