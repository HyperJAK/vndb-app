package com.zc.vndb.feature.profile.domain.repository

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.profile.domain.model.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeSessionRepository(initial: Session? = null) : SessionRepository {

    val current = MutableStateFlow(initial)
    override val session: StateFlow<Session?> = current

    /** Session returned for a successful log-in. */
    var acceptedSession: Session? = null
    var logInError: DomainError? = null
    val loggedInTokens = mutableListOf<String>()

    override suspend fun logIn(token: String): Result<Session> {
        loggedInTokens += token
        logInError?.let { return Result.failure(it) }
        val session = checkNotNull(acceptedSession)
        current.value = session
        return Result.success(session)
    }

    override suspend fun logOut() {
        current.value = null
    }
}
