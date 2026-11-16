package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseSummaryPageDTO(
    @SerialName("results") val results: List<ReleaseSummaryDTO>,
    @SerialName("more") val more: Boolean,
)
