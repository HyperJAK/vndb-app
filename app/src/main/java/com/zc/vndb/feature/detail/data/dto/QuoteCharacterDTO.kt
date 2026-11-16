package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuoteCharacterDTO(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String?,
)
