package com.zc.vndb.common.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

const val VNDB_API_BASE_URL = "https://api.vndb.org/kana/"

private const val REQUEST_TIMEOUT_MILLIS = 15_000L
private const val CONNECT_TIMEOUT_MILLIS = 10_000L

/** The single JSON configuration used for every VNDB request and response. */
val appJson: Json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}

/**
 * Builds the app-wide [HttpClient] for the VNDB Kana API.
 * Tests pass a `MockEngine` as [engine]; [enableLogging] is on for debug builds only.
 */
fun createHttpClient(
    json: Json,
    engine: HttpClientEngine = OkHttp.create(),
    enableLogging: Boolean = false,
): HttpClient = HttpClient(engine) {
    expectSuccess = true
    install(ContentNegotiation) { json(json) }
    install(HttpTimeout) {
        requestTimeoutMillis = REQUEST_TIMEOUT_MILLIS
        connectTimeoutMillis = CONNECT_TIMEOUT_MILLIS
    }
    if (enableLogging) {
        install(Logging) { level = LogLevel.INFO }
    }
    defaultRequest {
        url(VNDB_API_BASE_URL)
        contentType(ContentType.Application.Json)
    }
}
