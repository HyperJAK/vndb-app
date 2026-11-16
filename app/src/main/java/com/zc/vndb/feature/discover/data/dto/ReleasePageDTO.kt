package com.zc.vndb.feature.discover.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleasePageDTO(
    @SerialName("results") val results: List<ReleaseDTO>,
    @SerialName("more") val more: Boolean,
)
