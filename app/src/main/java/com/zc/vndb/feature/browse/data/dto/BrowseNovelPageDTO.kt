package com.zc.vndb.feature.browse.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BrowseNovelPageDTO(
    @SerialName("results") val results: List<BrowseNovelDTO>,
    @SerialName("more") val more: Boolean,
    @SerialName("count") val count: Int?,
)
