package com.zc.vndb.feature.profile.data.repository

import com.zc.vndb.common.data.util.safeApiCall
import com.zc.vndb.feature.profile.data.local.SessionLocalDataSource
import com.zc.vndb.feature.profile.data.mapper.toDomain
import com.zc.vndb.feature.profile.data.mapper.toStoredSession
import com.zc.vndb.feature.profile.data.remote.ProfileService
import com.zc.vndb.feature.profile.domain.model.Session
import com.zc.vndb.feature.profile.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionRepositoryImpl(
    private val service: ProfileService,
    private val local: SessionLocalDataSource,
) : SessionRepository {

    override val session: Flow<Session?> = local.session.map { it?.toDomain() }

    override suspend fun logIn(token: String): Result<Session> =
        safeApiCall { service.authInfo(token).toStoredSession(token) }
            .map { stored ->
                local.save(stored)
                stored.toDomain()
            }

    override suspend fun logOut() = local.clear()
}
