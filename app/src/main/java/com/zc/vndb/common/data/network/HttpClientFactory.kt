package com.zc.vndb.common.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

const val VNDB_API_HOST = "api.vndb.org"
const val VNDB_API_BASE_URL = "https://$VNDB_API_HOST/kana/"

private const val REQUEST_TIMEOUT_MILLIS = 15_000L
private const val CONNECT_TIMEOUT_MILLIS = 10_000L
private const val UNREAD_BODY = "<body not read>"
private val REDIRECT_CODES = 300..399
private val CLIENT_ERROR_CODES = 400..499
private val SERVER_ERROR_CODES = 500..599

/** The single JSON configuration used for every VNDB request and response. */
val appJson: Json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}

/**
 * Builds the app-wide [HttpClient] for the VNDB Kana API.
 * Tests pass a `MockEngine` as [engine]. With a [logger], every VNDB API request and response
 * (headers and JSON bodies) is logged; image downloads that share this client are not.
 */
fun createHttpClient(
    json: Json,
    engine: HttpClientEngine = OkHttp.create(),
    logger: Logger? = null,
): HttpClient = HttpClient(engine) {
    // VNDB answers errors with `Content-Type: text` (no subtype). Ktor's built-in validation reads
    // the body with that charset and fails to parse it, so every 4xx/5xx would surface as an unknown
    // error. Validate status codes ourselves, without reading the body.
    expectSuccess = false
    HttpResponseValidator {
        validateResponse { response ->
            when (response.status.value) {
                in REDIRECT_CODES -> throw RedirectResponseException(response, UNREAD_BODY)
                in CLIENT_ERROR_CODES -> throw ClientRequestException(response, UNREAD_BODY)
                in SERVER_ERROR_CODES -> throw ServerResponseException(response, UNREAD_BODY)
            }
        }
    }
    install(ContentNegotiation) { json(json) }
    install(HttpTimeout) {
        requestTimeoutMillis = REQUEST_TIMEOUT_MILLIS
        connectTimeoutMillis = CONNECT_TIMEOUT_MILLIS
    }
    if (logger != null) {
        install(Logging) {
            this.logger = logger
            level = LogLevel.ALL
            filter { request -> request.url.host == VNDB_API_HOST }
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
    }
    defaultRequest {
        url(VNDB_API_BASE_URL)
        contentType(ContentType.Application.Json)
    }
}
