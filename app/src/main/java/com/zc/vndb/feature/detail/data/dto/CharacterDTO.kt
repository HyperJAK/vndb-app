package com.zc.vndb.feature.detail.data.dto

import com.zc.vndb.common.data.dto.ImageDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterDTO(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("original") val original: String?,
    @SerialName("image") val image: ImageDTO?,
    @SerialName("vns") val vns: List<CharacterNovelDTO>?,
)
