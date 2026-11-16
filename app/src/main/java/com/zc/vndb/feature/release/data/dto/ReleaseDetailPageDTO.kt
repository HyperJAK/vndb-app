package com.zc.vndb.feature.release.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseDetailPageDTO(@SerialName("results") val results: List<ReleaseDetailDTO>)
