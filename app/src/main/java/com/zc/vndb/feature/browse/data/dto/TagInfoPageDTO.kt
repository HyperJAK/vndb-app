package com.zc.vndb.feature.browse.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagInfoPageDTO(@SerialName("results") val results: List<TagInfoDTO>)
