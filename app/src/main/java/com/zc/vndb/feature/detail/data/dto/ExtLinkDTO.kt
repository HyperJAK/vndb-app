package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExtLinkDTO(@SerialName("label") val label: String, @SerialName("url") val url: String)
