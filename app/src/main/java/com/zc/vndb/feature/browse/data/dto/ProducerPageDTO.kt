package com.zc.vndb.feature.browse.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProducerPageDTO(@SerialName("results") val results: List<ProducerDTO>)
