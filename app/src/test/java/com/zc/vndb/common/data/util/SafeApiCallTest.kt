package com.zc.vndb.common.data.util

import com.zc.vndb.common.data.network.appJson
import com.zc.vndb.common.data.network.createHttpClient
import com.zc.vndb.common.domain.model.DomainError
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import kotlinx.serialization.Serializable

class SafeApiCallTest {

    @Serializable
    private data class StatsResponse(val vn: Int)

    private fun call(handler: MockRequestHandler) = suspend {
        createHttpClient(appJson, MockEngine(handler)).get("stats").body<StatsResponse>()
    }

    private fun jsonResponse(
        body: String,
        status: HttpStatusCode = HttpStatusCode.OK,
    ): MockRequestHandler = {
        respond(
            body,
            status,
            headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
        )
    }

    @Test
    fun `returns the parsed body on success`() = runTest {
        val result = safeApiCall(call(jsonResponse("""{"vn":68053,"releases":1}""")))
        assertEquals(StatsResponse(vn = 68053), result.getOrThrow())
    }

    @Test
    fun `maps 404 to NotFound`() = runTest {
        assertEquals(
            DomainError.NotFound,
            safeApiCall(call(jsonResponse("{}", HttpStatusCode.NotFound))).exceptionOrNull(),
        )
    }

    @Test
    fun `maps 401 and 403 to Unauthorized`() = runTest {
        listOf(HttpStatusCode.Unauthorized, HttpStatusCode.Forbidden).forEach { status ->
            assertEquals(
                DomainError.Unauthorized,
                safeApiCall(call(jsonResponse("{}", status))).exceptionOrNull(),
            )
        }
    }

    @Test
    fun `maps 429 to RateLimited`() = runTest {
        val result = safeApiCall(call(jsonResponse("{}", HttpStatusCode.TooManyRequests)))
        assertEquals(DomainError.RateLimited, result.exceptionOrNull())
    }

    @Test
    fun `maps other 4xx to Unknown`() = runTest {
        assertIs<DomainError.Unknown>(
            safeApiCall(call(jsonResponse("{}", HttpStatusCode.BadRequest))).exceptionOrNull(),
        )
    }

    @Test
    fun `maps 5xx to Server`() = runTest {
        val result = safeApiCall(call(jsonResponse("{}", HttpStatusCode.InternalServerError)))
        assertEquals(DomainError.Server, result.exceptionOrNull())
    }

    @Test
    fun `maps malformed JSON to InvalidData`() = runTest {
        assertEquals(
            DomainError.InvalidData,
            safeApiCall(call(jsonResponse("""{"vn":"many"}"""))).exceptionOrNull(),
        )
    }

    @Test
    fun `maps a request timeout to Timeout`() = runTest {
        val result = safeApiCall { throw HttpRequestTimeoutException("stats", null) }
        assertEquals(DomainError.Timeout, result.exceptionOrNull())
    }

    @Test
    fun `maps IO failures to Network`() = runTest {
        assertEquals(
            DomainError.Network,
            safeApiCall(call { throw IOException("offline") }).exceptionOrNull(),
        )
    }

    @Test
    fun `keeps a DomainError thrown by the block`() = runTest {
        assertEquals(
            DomainError.NotFound,
            safeApiCall { throw DomainError.NotFound }.exceptionOrNull(),
        )
    }

    @Test
    fun `rethrows cancellation`() = runTest {
        assertFailsWith<CancellationException> {
            safeApiCall {
                throw CancellationException(
                    "cancelled",
                )
            }
        }
    }

    @Test
    fun `maps VNDB's 401 with a malformed text content type to Unauthorized`() = runTest {
        val handler: MockRequestHandler = {
            respond(
                "Invalid token.",
                HttpStatusCode.Unauthorized,
                headersOf(HttpHeaders.ContentType, "text"),
            )
        }
        assertEquals(DomainError.Unauthorized, safeApiCall(call(handler)).exceptionOrNull())
    }
}
