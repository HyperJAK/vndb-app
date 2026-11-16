package com.zc.vndb.feature.release.data.dto

import com.zc.vndb.common.data.dto.ImageDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseNovelDTO(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String?,
    @SerialName("rtype") val rtype: String?,
    @SerialName("released") val released: String?,
    @SerialName("image") val image: ImageDTO?,
    @SerialName("alttitle") val altTitle: String? = null,
)
