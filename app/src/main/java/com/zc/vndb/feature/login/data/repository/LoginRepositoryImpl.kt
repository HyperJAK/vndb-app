package com.zc.vndb.feature.login.data.repository

import com.zc.vndb.common.data.local.SessionLocalDataSource
import com.zc.vndb.common.data.mapper.toDomain
import com.zc.vndb.common.data.util.safeApiCall
import com.zc.vndb.common.domain.model.Session
import com.zc.vndb.feature.login.data.mapper.toStoredSession
import com.zc.vndb.feature.login.data.remote.LoginService
import com.zc.vndb.feature.login.domain.repository.LoginRepository

class LoginRepositoryImpl(
    private val service: LoginService,
    private val local: SessionLocalDataSource,
) : LoginRepository {

    override suspend fun logIn(token: String): Result<Session> =
        safeApiCall { service.authInfo(token).toStoredSession(token) }
            .map { stored ->
                local.save(stored)
                stored.toDomain()
            }
}
