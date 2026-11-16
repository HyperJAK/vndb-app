package com.zc.vndb.common.domain.model

/** The signed-in VNDB account (shared by the login and profile features). The token never leaves `data`. */
data class Session(
    val userId: String,
    val username: String,
    /** The token has `listread`: private labels and entries are visible. */
    val canReadPrivateList: Boolean,
    /** The token has `listwrite`: the list can be edited from the app. */
    val canWriteList: Boolean = false,
)
