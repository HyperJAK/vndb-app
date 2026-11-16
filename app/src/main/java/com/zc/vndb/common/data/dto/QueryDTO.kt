package com.zc.vndb.common.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/** Body of a VNDB Kana query (`POST /vn`, `POST /release`, …). Null fields are omitted. */
@Serializable
data class QueryDTO(
    @SerialName("filters") val filters: JsonElement,
    @SerialName("fields") val fields: String,
    @SerialName("sort") val sort: String?,
    @SerialName("reverse") val reverse: Boolean?,
    @SerialName("results") val results: Int,
    @SerialName("page") val page: Int?,
    @SerialName("count") val count: Boolean?,
)
