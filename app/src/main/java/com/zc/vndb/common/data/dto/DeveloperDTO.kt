package com.zc.vndb.common.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A developer on a VN. [id] is only present when the query asks for `developers.id`. */
@Serializable
data class DeveloperDTO(
    @SerialName("name") val name: String,
    @SerialName("id") val id: String? = null,
)
