package com.zc.vndb.feature.login.data.repository

import com.zc.vndb.common.data.local.SessionLocalDataSource
import com.zc.vndb.common.data.local.sessionStore
import com.zc.vndb.common.data.network.appJson
import com.zc.vndb.common.data.network.createHttpClient
import com.zc.vndb.common.data.repository.SessionRepositoryImpl
import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.login.data.remote.LoginService
import com.zc.vndb.feature.login.domain.model.LoginSamples
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.rules.TemporaryFolder

class LoginRepositoryImplTest {

    @get:Rule
    val folder = TemporaryFolder()

    private val requests = mutableListOf<Pair<String, String?>>()
    private var status = HttpStatusCode.OK

    private fun file() = File(folder.root, "session.preferences_pb")

    private fun TestScope.setUp(): Pair<LoginRepositoryImpl, SessionLocalDataSource> {
        val local = sessionStore(file())
        val client = createHttpClient(
            json = appJson,
            engine = MockEngine { request ->
                requests += request.url.encodedPath.removePrefix(
                    "/kana/",
                ) to request.headers[HttpHeaders.Authorization]
                if (status == HttpStatusCode.OK) {
                    respond(
                        """{"id":"u3","username":"ayo","permissions":["listread"]}""",
                        status,
                        headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
                    )
                } else {
                    // VNDB's real error response: a malformed "text" content type.
                    respond("Invalid token.", status, headersOf(HttpHeaders.ContentType, "text"))
                }
            },
        )
        return LoginRepositoryImpl(LoginService(client), local) to local
    }

    @Test
    fun `log in validates the token, stores it encrypted and starts the session`() = runTest {
        val (repository, local) = setUp()

        val session = repository.logIn(LoginSamples.TOKEN).getOrThrow()

        assertEquals("authinfo" to "Token ${LoginSamples.TOKEN}", requests.single())
        assertEquals("u3", session.userId)
        assertEquals("ayo", session.username)
        assertTrue(session.canReadPrivateList)
        assertEquals(session, SessionRepositoryImpl(local).session.first())
        assertTrue(LoginSamples.TOKEN !in file().readBytes().decodeToString())
    }

    @Test
    fun `a rejected token is not stored`() = runTest {
        val (repository, local) = setUp()
        status = HttpStatusCode.Unauthorized

        assertEquals(
            DomainError.Unauthorized,
            repository.logIn(LoginSamples.TOKEN).exceptionOrNull(),
        )
        assertNull(local.token())
    }

    @Test
    fun `log out removes the token`() = runTest {
        val (repository, local) = setUp()
        repository.logIn(LoginSamples.TOKEN)

        SessionRepositoryImpl(local).logOut()

        assertNull(local.token())
        assertNull(SessionRepositoryImpl(local).session.first())
    }
}
