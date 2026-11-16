package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoverReleasePageDTO(@SerialName("results") val results: List<CoverReleaseDTO>)
