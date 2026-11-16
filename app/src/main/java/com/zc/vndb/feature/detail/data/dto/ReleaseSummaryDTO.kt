package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseSummaryDTO(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String?,
    @SerialName("released") val released: String?,
    @SerialName("platforms") val platforms: List<String>?,
    @SerialName("languages") val languages: List<EditionLanguageDTO>?,
    @SerialName("official") val official: Boolean?,
    @SerialName("patch") val patch: Boolean?,
    @SerialName("freeware") val freeware: Boolean?,
    @SerialName("minage") val minage: Int?,
)
