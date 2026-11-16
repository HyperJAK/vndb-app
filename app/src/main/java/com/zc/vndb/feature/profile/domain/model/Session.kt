package com.zc.vndb.feature.profile.domain.model

/** The signed-in VNDB account. The token itself never leaves `data`. */
data class Session(
    val userId: String,
    val username: String,
    /** The token has `listread`: private labels and entries are visible. */
    val canReadPrivateList: Boolean,
)
