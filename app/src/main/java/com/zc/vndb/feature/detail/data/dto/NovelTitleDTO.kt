package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NovelTitleDTO(
    @SerialName("lang") val lang: String,
    @SerialName("title") val title: String,
    @SerialName("latin") val latin: String?,
    @SerialName("official") val official: Boolean?,
    @SerialName("main") val main: Boolean?,
)
