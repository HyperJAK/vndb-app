package com.zc.vndb.feature.detail.domain.model

/** Staff and voice actors, from the VN record. */
data class NovelCredits(
    val staff: List<StaffCredit>,
    val editions: List<StaffEdition>,
    /** Character id → voice actor name. */
    val voiceActors: Map<String, String>,
)
