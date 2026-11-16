package com.zc.vndb.feature.release.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseLanguageDTO(
    @SerialName("lang") val lang: String,
    @SerialName("title") val title: String?,
    @SerialName("main") val main: Boolean?,
    @SerialName("mtl") val mtl: Boolean?,
)
