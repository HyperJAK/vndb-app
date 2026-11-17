package com.zc.vndb.feature.browse.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A producer record from `POST /producer`. */
@Serializable
data class ProducerDTO(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("original") val original: String?,
    @SerialName("aliases") val aliases: List<String>?,
    @SerialName("lang") val lang: String?,
    /** `co` company, `in` individual or `ng` amateur group. */
    @SerialName("type") val type: String?,
    @SerialName("description") val description: String?,
)
