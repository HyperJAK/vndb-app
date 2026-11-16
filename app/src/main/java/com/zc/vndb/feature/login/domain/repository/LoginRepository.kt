package com.zc.vndb.feature.login.domain.repository

import com.zc.vndb.common.domain.model.Session

interface LoginRepository {

    /** Validates [token] with VNDB and, if it is valid, stores it on the device. */
    suspend fun logIn(token: String): Result<Session>
}
