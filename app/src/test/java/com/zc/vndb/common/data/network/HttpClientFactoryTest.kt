package com.zc.vndb.common.data.network

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.content.OutgoingContent
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class HttpClientFactoryTest {

    @Test
    fun `relative paths resolve against the Kana base URL and send JSON`() = runTest {
        var sentUrl = ""
        var sentContentType: ContentType? = null
        val engine = MockEngine { request ->
            sentUrl = request.url.toString()
            val body: OutgoingContent = request.body
            assertIs<TextContent>(body)
            sentContentType = body.contentType
            respondOk()
        }

        createHttpClient(appJson, engine).post("vn") { setBody("""{"results":1}""") }

        assertEquals("https://api.vndb.org/kana/vn", sentUrl)
        assertEquals(ContentType.Application.Json, sentContentType?.withoutParameters())
    }

    @Test
    fun `appJson tolerates unknown keys and omits nulls`() {
        assertTrue(appJson.configuration.ignoreUnknownKeys)
        assertFalse(appJson.configuration.explicitNulls)
    }

    private class RecordingLogger : Logger {
        val lines = mutableListOf<String>()
        override fun log(message: String) {
            lines += message
        }
    }

    @Test
    fun `logs VNDB API requests and responses, with the auth token redacted`() = runTest {
        val logger = RecordingLogger()
        val engine = MockEngine {
            respond(
                """{"vn":68053}""",
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }

        createHttpClient(appJson, engine, logger).post("vn") {
            header(HttpHeaders.Authorization, "Token secret-token")
            setBody("""{"results":1}""")
        }

        val log = logger.lines.joinToString("\n")
        assertTrue("https://api.vndb.org/kana/vn" in log)
        assertTrue("""{"results":1}""" in log)
        assertTrue("""{"vn":68053}""" in log)
        assertFalse("secret-token" in log)
    }

    @Test
    fun `does not log image downloads`() = runTest {
        val logger = RecordingLogger()
        val engine = MockEngine { respondOk() }

        createHttpClient(appJson, engine, logger).get("https://t.vndb.org/cv/19/77819.jpg")

        assertTrue(logger.lines.isEmpty())
    }
}
