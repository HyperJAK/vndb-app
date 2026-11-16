package com.zc.vndb.common.domain.repository

import com.zc.vndb.common.domain.model.Session
import kotlinx.coroutines.flow.Flow

/** The VNDB account signed in on this device. Logging in is the `login` feature's job. */
interface SessionRepository {

    /** The signed-in account, or null for a guest. */
    val session: Flow<Session?>

    /** Deletes the stored token. */
    suspend fun logOut()
}
