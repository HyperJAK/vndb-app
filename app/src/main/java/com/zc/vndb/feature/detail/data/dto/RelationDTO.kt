package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RelationDTO(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String?,
    @SerialName("relation") val relation: String?,
    @SerialName("relation_official") val relationOfficial: Boolean?,
    @SerialName("released") val released: String?,
    @SerialName("rating") val rating: Double?,
    @SerialName("alttitle") val altTitle: String? = null,
)
