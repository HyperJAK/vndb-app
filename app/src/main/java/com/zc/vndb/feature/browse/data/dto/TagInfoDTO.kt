package com.zc.vndb.feature.browse.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A tag record from `POST /tag`. */
@Serializable
data class TagInfoDTO(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("aliases") val aliases: List<String>?,
    @SerialName("description") val description: String?,
    @SerialName("category") val category: String?,
    @SerialName("vn_count") val vnCount: Int?,
)
