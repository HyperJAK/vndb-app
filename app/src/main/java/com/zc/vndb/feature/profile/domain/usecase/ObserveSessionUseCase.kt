package com.zc.vndb.feature.profile.domain.usecase

import com.zc.vndb.common.domain.model.Session
import com.zc.vndb.common.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class ObserveSessionUseCase(private val repository: SessionRepository) {

    operator fun invoke(): Flow<Session?> = repository.session.distinctUntilChanged()
}
