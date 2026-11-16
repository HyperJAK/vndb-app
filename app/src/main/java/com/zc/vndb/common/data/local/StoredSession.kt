package com.zc.vndb.common.data.local

/** What is kept on the device for the signed-in account. */
data class StoredSession(
    val token: String,
    val userId: String,
    val username: String,
    val permissions: Set<String>,
)
