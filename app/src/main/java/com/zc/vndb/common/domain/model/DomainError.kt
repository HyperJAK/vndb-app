package com.zc.vndb.common.domain.model

/**
 * Failures that may cross from `data` into `domain` and `presentation`.
 * Repositories produce them through `safeApiCall`; no HTTP or Ktor types ever leave `data`.
 */
sealed class DomainError(cause: Throwable? = null) : Exception(cause) {
    data object Network : DomainError()
    data object Timeout : DomainError()
    data object Unauthorized : DomainError()
    data object NotFound : DomainError()
    data object RateLimited : DomainError()
    data object Server : DomainError()
    data object InvalidData : DomainError()
    data class Unknown(override val cause: Throwable) : DomainError(cause)
}
