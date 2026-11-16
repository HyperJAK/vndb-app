package com.zc.vndb.feature.login.domain.repository

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.domain.model.Session

class FakeLoginRepository : LoginRepository {

    /** Session returned for a successful log-in. */
    var acceptedSession: Session? = null
    var logInError: DomainError? = null
    val loggedInTokens = mutableListOf<String>()

    /** The last session logged in, as the shared session store would hold it. */
    var stored: Session? = null
        private set

    override suspend fun logIn(token: String): Result<Session> {
        loggedInTokens += token
        logInError?.let { return Result.failure(it) }
        val session = checkNotNull(acceptedSession)
        stored = session
        return Result.success(session)
    }
}
