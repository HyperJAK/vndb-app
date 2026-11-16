package com.zc.vndb.common.data.repository

import com.zc.vndb.common.data.local.SessionLocalDataSource
import com.zc.vndb.common.data.mapper.toDomain
import com.zc.vndb.common.domain.model.Session
import com.zc.vndb.common.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionRepositoryImpl(private val local: SessionLocalDataSource) : SessionRepository {

    override val session: Flow<Session?> = local.session.map { it?.toDomain() }

    override suspend fun logOut() = local.clear()
}
