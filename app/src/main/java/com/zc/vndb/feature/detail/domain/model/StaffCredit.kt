package com.zc.vndb.feature.detail.domain.model

data class StaffCredit(
    val staffId: String,
    val name: String,
    val role: StaffCreditRole,
    /** E.g. "First OP & ED production". */
    val note: String?,
    /** Null for the original release; otherwise a [StaffEdition] id. */
    val editionId: Int?,
)
