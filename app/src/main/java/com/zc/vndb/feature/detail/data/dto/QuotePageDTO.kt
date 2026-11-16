package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuotePageDTO(
    @SerialName("results") val results: List<QuoteDTO>,
    @SerialName("more") val more: Boolean,
)
