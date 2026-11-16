package com.zc.vndb.feature.discover.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseLanguageDTO(
    @SerialName("lang") val lang: String,
    @SerialName("mtl") val mtl: Boolean?,
)
