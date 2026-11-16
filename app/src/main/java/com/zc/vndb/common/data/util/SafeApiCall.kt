package com.zc.vndb.common.data.util

import com.zc.vndb.common.data.network.VndbRejectedException
import com.zc.vndb.common.domain.model.DomainError
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.ContentConvertException
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

/**
 * Runs a service call and maps every Ktor / serialization failure to a [DomainError].
 * This is the only place in the app that knows those exception types. Cancellation is rethrown.
 */
@Suppress("TooGenericExceptionCaught") // Unknown failures must become DomainError.Unknown instead of crashing.
suspend fun <T> safeApiCall(block: suspend () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e.toDomainError())
    }

private fun Throwable.toDomainError(): DomainError = when (this) {
    is DomainError -> this
    is VndbRejectedException -> DomainError.Rejected(reason)
    is HttpRequestTimeoutException, is ConnectTimeoutException, is SocketTimeoutException -> DomainError.Timeout
    is ClientRequestException -> response.status.toDomainError(this)
    is ServerResponseException -> DomainError.Server
    is ResponseException -> DomainError.Unknown(this)
    is SerializationException, is ContentConvertException -> DomainError.InvalidData
    is IOException -> DomainError.Network
    else -> DomainError.Unknown(this)
}

private fun HttpStatusCode.toDomainError(cause: Throwable): DomainError = when (this) {
    HttpStatusCode.Unauthorized, HttpStatusCode.Forbidden -> DomainError.Unauthorized
    HttpStatusCode.NotFound -> DomainError.NotFound
    HttpStatusCode.TooManyRequests -> DomainError.RateLimited
    else -> DomainError.Unknown(cause)
}
