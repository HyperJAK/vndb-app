package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuoteDTO(
    @SerialName("id") val id: String,
    @SerialName("quote") val quote: String,
    @SerialName("score") val score: Int?,
    @SerialName("character") val character: QuoteCharacterDTO?,
)
