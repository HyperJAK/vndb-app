package com.zc.vndb.common.domain.repository

import com.zc.vndb.common.domain.model.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeSessionRepository(initial: Session? = null) : SessionRepository {

    val current = MutableStateFlow(initial)
    override val session: StateFlow<Session?> = current

    override suspend fun logOut() {
        current.value = null
    }
}
