package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A VNDB release, as needed for the editions list. */
@Serializable
data class EditionDTO(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String?,
    @SerialName("released") val released: String?,
    @SerialName("platforms") val platforms: List<String>?,
    @SerialName("languages") val languages: List<EditionLanguageDTO>?,
    @SerialName("official") val official: Boolean?,
    @SerialName("patch") val patch: Boolean?,
    @SerialName("minage") val minage: Int?,
)
