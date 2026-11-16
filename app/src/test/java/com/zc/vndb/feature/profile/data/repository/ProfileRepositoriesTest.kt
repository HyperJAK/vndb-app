package com.zc.vndb.feature.profile.data.repository

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.zc.vndb.common.data.network.appJson
import com.zc.vndb.common.data.network.createHttpClient
import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.domain.util.DispatcherProvider
import com.zc.vndb.feature.profile.data.local.SessionLocalDataSource
import com.zc.vndb.feature.profile.data.local.TokenCipher
import com.zc.vndb.feature.profile.data.remote.ProfileService
import com.zc.vndb.feature.profile.domain.model.ProfileSamples
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.jsonObject
import org.junit.Rule
import org.junit.rules.TemporaryFolder

class ProfileRepositoriesTest {

    @get:Rule
    val folder = TemporaryFolder()

    private val requests = mutableListOf<Pair<String, String?>>()
    private val bodies = mutableMapOf<String, String>()
    private var status = HttpStatusCode.OK

    /** Reversible, so tests can check the stored value is not the plain token. */
    private class FakeCipher : TokenCipher {
        override fun encrypt(plain: String) = "enc:" + plain.reversed()
        override fun decrypt(encoded: String) = encoded.removePrefix("enc:").reversed()
    }

    private fun TestScope.setUp(): Triple<SessionRepositoryImpl, ProfileRepositoryImpl, SessionLocalDataSource> {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val dispatchers = object : DispatcherProvider {
            override val main: CoroutineDispatcher = dispatcher
            override val io: CoroutineDispatcher = dispatcher
            override val default: CoroutineDispatcher = dispatcher
        }
        val file = File(folder.root, "session.preferences_pb")
        val dataStore = PreferenceDataStoreFactory.create(scope = backgroundScope) { file }
        val local = SessionLocalDataSource(dataStore, FakeCipher(), dispatchers)
        val client = createHttpClient(
            json = appJson,
            engine = MockEngine { request ->
                val path = request.url.encodedPath.removePrefix("/kana/")
                requests += path to request.headers[HttpHeaders.Authorization]
                if (path == "ulist") bodies[path] = request.body.toByteArray().decodeToString()
                respond(
                    content = if (status == HttpStatusCode.OK) {
                        RESPONSES.getValue(
                            path,
                        )
                    } else {
                        "Invalid token."
                    },
                    status = status,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        ContentType.Application.Json.toString(),
                    ),
                )
            },
        )
        val service = ProfileService(client)
        return Triple(
            SessionRepositoryImpl(service, local),
            ProfileRepositoryImpl(service, local),
            local,
        )
    }

    @Test
    fun `log in validates the token, stores it encrypted and exposes the session`() = runTest {
        val (sessions, _, _) = setUp()

        val session = sessions.logIn(ProfileSamples.TOKEN).getOrThrow()

        assertEquals("authinfo" to "Token ${ProfileSamples.TOKEN}", requests.single())
        assertEquals("u3", session.userId)
        assertEquals("ayo", session.username)
        assertTrue(session.canReadPrivateList)
        assertEquals(session, sessions.session.first())
        val stored = File(folder.root, "session.preferences_pb").readBytes().decodeToString()
        assertTrue(ProfileSamples.TOKEN !in stored)
    }

    @Test
    fun `a rejected token is not stored`() = runTest {
        val (sessions, _, _) = setUp()
        status = HttpStatusCode.Unauthorized

        assertEquals(
            DomainError.Unauthorized,
            sessions.logIn(ProfileSamples.TOKEN).exceptionOrNull(),
        )
        assertNull(sessions.session.first())
    }

    @Test
    fun `list requests carry the token and map labels and entries`() = runTest {
        val (sessions, profile, _) = setUp()
        sessions.logIn(ProfileSamples.TOKEN)
        requests.clear()

        val listLabels = profile.getLabels("u3").getOrThrow()
        val page = profile.getEntries("u3", labelId = 1, page = 2, pageSize = 25).getOrThrow()

        assertEquals(listOf(1, 7, 10), listLabels.map { it.id })
        assertTrue(listLabels.single { it.id == 10 }.isPrivate)
        assertTrue(requests.all { it.second == "Token ${ProfileSamples.TOKEN}" })
        val query = appJson.parseToJsonElement(bodies.getValue("ulist")).jsonObject
        assertEquals("\"u3\"", query["user"].toString())
        assertEquals("[\"label\",\"=\",1]", query["filters"].toString())
        assertEquals("2", query["page"].toString())
        with(page.entries.single()) {
            assertEquals("v58641", novelId)
            assertEquals(85, vote)
            assertEquals("2026-01-07", started.toString())
            assertNull(finished)
            assertEquals(listOf(1, 7), labels.map { it.id })
        }
        assertTrue(page.hasMore)
    }

    @Test
    fun `a token rejected later signs the user out`() = runTest {
        val (sessions, profile, _) = setUp()
        sessions.logIn(ProfileSamples.TOKEN)
        status = HttpStatusCode.Unauthorized

        assertEquals(DomainError.Unauthorized, profile.getLabels("u3").exceptionOrNull())
        assertNull(sessions.session.first())
    }

    @Test
    fun `log out removes the token`() = runTest {
        val (sessions, _, local) = setUp()
        sessions.logIn(ProfileSamples.TOKEN)

        sessions.logOut()

        assertNull(local.token())
    }

    @Test
    fun `user lookup reads play-time votes`() = runTest {
        val (_, profile, _) = setUp()

        val user = profile.getUser("u3").getOrThrow()

        assertEquals(9, user.lengthVotes)
        assertEquals(9685, user.lengthVotesMinutes)
        assertNull(requests.single().second)
    }

    private companion object {
        val RESPONSES = mapOf(
            "authinfo" to """{"id":"u3","username":"ayo","permissions":["listread"]}""",
            "user" to
                """{"u3":{"id":"u3","username":"ayo","lengthvotes":9,"lengthvotes_sum":9685}}""",
            "ulist_labels" to """{"labels":[
                {"id":1,"label":"Playing","private":false,"count":1},
                {"id":7,"label":"Voted","private":false,"count":5},
                {"id":10,"label":"Secret","private":true,"count":2}]}""",
            "ulist" to """{"more":true,"results":[{"id":"v58641","vote":85,"lastmod":1767815893,
                "started":"2026-01-07","finished":null,"notes":"","labels":[{"id":1,"label":"Playing"},
                {"id":7,"label":"Voted"}],"vn":{"title":"Z.A.T.O.","alttitle":null,"released":"2025-11-11",
                "image":{"url":"https://t.vndb.org/cv/81/119181.jpg","thumbnail":"https://t.vndb.org/cv.t/81/119181.jpg",
                "sexual":0,"violence":0}}}]}""",
        )
    }
}
