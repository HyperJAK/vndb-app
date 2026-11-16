package com.zc.vndb.feature.detail.domain.model

/** Everything on the Cast & Staff tab. */
data class NovelCast(
    /** All characters, main first, with voice actors filled in. */
    val characters: List<NovelCharacter>,
    val staff: List<StaffCredit>,
    val editions: List<StaffEdition>,
)
