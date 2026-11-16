package com.zc.vndb.feature.release.data.dto

import com.zc.vndb.common.data.dto.ExtLinkDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseDetailDTO(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String?,
    @SerialName("alttitle") val alttitle: String?,
    @SerialName("released") val released: String?,
    @SerialName("minage") val minage: Int?,
    @SerialName("official") val official: Boolean?,
    @SerialName("patch") val patch: Boolean?,
    @SerialName("freeware") val freeware: Boolean?,
    @SerialName("uncensored") val uncensored: Boolean?,
    @SerialName("has_ero") val hasEro: Boolean?,
    @SerialName("platforms") val platforms: List<String>?,
    @SerialName("media") val media: List<ReleaseMediumDTO>?,
    /** [width, height]. */
    @SerialName("resolution") val resolution: List<Int>?,
    @SerialName("engine") val engine: String?,
    @SerialName("voiced") val voiced: Int?,
    @SerialName("catalog") val catalog: String?,
    @SerialName("gtin") val gtin: String?,
    @SerialName("notes") val notes: String?,
    @SerialName("languages") val languages: List<ReleaseLanguageDTO>?,
    @SerialName("vns") val vns: List<ReleaseNovelDTO>?,
    @SerialName("producers") val producers: List<ReleaseProducerDTO>?,
    @SerialName("images") val images: List<ReleaseImageDTO>?,
    @SerialName("extlinks") val extlinks: List<ExtLinkDTO>?,
)
