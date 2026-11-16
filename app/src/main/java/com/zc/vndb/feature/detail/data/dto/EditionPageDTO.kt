package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditionPageDTO(@SerialName("results") val results: List<EditionDTO>)
