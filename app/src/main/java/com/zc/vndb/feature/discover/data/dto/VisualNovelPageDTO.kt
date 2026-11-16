package com.zc.vndb.feature.discover.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VisualNovelPageDTO(
    @SerialName("results") val results: List<VisualNovelDTO>,
    @SerialName("more") val more: Boolean,
)
