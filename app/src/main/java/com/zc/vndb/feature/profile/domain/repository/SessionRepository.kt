package com.zc.vndb.feature.profile.domain.repository

import com.zc.vndb.feature.profile.domain.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

    /** The signed-in account, or null for a guest. */
    val session: Flow<Session?>

    /** Validates [token] with VNDB and, if it is valid, stores it on the device. */
    suspend fun logIn(token: String): Result<Session>

    /** Deletes the stored token. */
    suspend fun logOut()
}
