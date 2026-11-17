package com.zc.vndb.feature.detail.domain.model

import com.zc.vndb.common.domain.model.VoiceActor

/** Staff and voice actors, from the VN record. */
data class NovelCredits(
    val staff: List<StaffCredit>,
    val editions: List<StaffEdition>,
    /** Character id → voice actor. */
    val voiceActors: Map<String, VoiceActor>,
)
