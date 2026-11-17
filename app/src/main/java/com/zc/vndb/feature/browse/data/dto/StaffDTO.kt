package com.zc.vndb.feature.browse.data.dto

import com.zc.vndb.common.data.dto.ExtLinkDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A staff record from `POST /staff`. */
@Serializable
data class StaffDTO(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("original") val original: String?,
    @SerialName("lang") val lang: String?,
    /** `m` or `f`; null when VNDB does not know. */
    @SerialName("gender") val gender: String?,
    @SerialName("aliases") val aliases: List<StaffAliasDTO>?,
    @SerialName("description") val description: String?,
    @SerialName("extlinks") val extlinks: List<ExtLinkDTO>?,
)

/** One name the person has worked under; [latin] is its romanisation, when it has one. */
@Serializable
data class StaffAliasDTO(
    @SerialName("name") val name: String,
    @SerialName("latin") val latin: String?,
)
