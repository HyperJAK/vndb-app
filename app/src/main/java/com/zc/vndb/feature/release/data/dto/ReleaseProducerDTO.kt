package com.zc.vndb.feature.release.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseProducerDTO(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("developer") val developer: Boolean?,
    @SerialName("publisher") val publisher: Boolean?,
)
