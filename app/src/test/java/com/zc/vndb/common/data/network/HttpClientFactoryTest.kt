package com.zc.vndb.common.data.network

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.content.OutgoingContent
import io.ktor.http.content.TextContent
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
}
