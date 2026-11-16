package com.zc.vndb.feature.discover.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseDTO(
    @SerialName("id") val id: String,
    @SerialName("released") val released: String?,
    @SerialName("patch") val patch: Boolean?,
    @SerialName("platforms") val platforms: List<String>?,
    @SerialName("languages") val languages: List<ReleaseLanguageDTO>?,
    @SerialName("vns") val vns: List<ReleaseVisualNovelDTO>?,
)
