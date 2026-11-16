package com.zc.vndb.feature.discover.data.remote

import com.zc.vndb.common.data.network.appJson
import com.zc.vndb.common.data.network.createHttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.serialization.json.JsonElement

/** A request the stub received. */
data class RecordedRequest(val method: HttpMethod, val path: String, val body: JsonElement?)

/**
 * A [DiscoverService] backed by a Ktor MockEngine. Responds to every request with [body] and [status],
 * and records each request in [requests].
 */
class DiscoverApiStub(var body: String = "{}", var status: HttpStatusCode = HttpStatusCode.OK) {
    val requests = mutableListOf<RecordedRequest>()

    val service = DiscoverService(
        createHttpClient(
            json = appJson,
            engine = MockEngine { request ->
                val text = request.body.toByteArray().decodeToString()
                requests += RecordedRequest(
                    method = request.method,
                    path = request.url.encodedPath,
                    body = text.takeIf { it.isNotEmpty() }?.let(appJson::parseToJsonElement),
                )
                respond(
                    body,
                    status,
                    headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
                )
            },
        ),
    )

    companion object {
        /** Reads a fixture captured from the live VNDB API (src/test/resources/discover). */
        fun fixture(name: String): String = checkNotNull(
            DiscoverApiStub::class.java.getResource("/discover/$name"),
        ) { "Missing fixture $name" }
            .readText()
    }
}
