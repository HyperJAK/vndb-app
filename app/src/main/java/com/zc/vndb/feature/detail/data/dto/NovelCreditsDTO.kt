package com.zc.vndb.feature.detail.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** The VN record's credit fields only. */
@Serializable
data class NovelCreditsDTO(
    @SerialName("id") val id: String,
    @SerialName("staff") val staff: List<StaffCreditDTO>?,
    @SerialName("editions") val editions: List<StaffEditionDTO>?,
    @SerialName("va") val va: List<VoiceActorDTO>?,
)
