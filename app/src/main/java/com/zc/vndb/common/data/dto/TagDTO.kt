package com.zc.vndb.common.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagDTO(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("rating") val rating: Double?,
    @SerialName("spoiler") val spoiler: Int?,
    @SerialName("category") val category: String?,
)
