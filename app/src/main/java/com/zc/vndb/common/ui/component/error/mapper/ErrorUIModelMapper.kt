package com.zc.vndb.common.ui.component.error.mapper

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.ui.component.error.model.ErrorUIModel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText

/** Maps any failure to the shared error UI. Unknown errors get a generic, retryable message. */
fun Throwable.toErrorUIModel(): ErrorUIModel = when (this) {
    DomainError.Network, DomainError.Timeout -> errorModel(
        UiText.StringResource(StringResource.error_network),
        isRetryable = true,
    )

    DomainError.NotFound -> errorModel(
        UiText.StringResource(StringResource.error_not_found),
        isRetryable = false,
    )

    DomainError.RateLimited -> errorModel(
        UiText.StringResource(StringResource.error_rate_limited),
        isRetryable = true,
    )

    DomainError.Unauthorized -> errorModel(
        UiText.StringResource(StringResource.error_unauthorized),
        isRetryable = false,
    )

    DomainError.Server -> errorModel(
        UiText.StringResource(StringResource.error_server),
        isRetryable = true,
    )

    else -> errorModel(UiText.StringResource(StringResource.error_generic), isRetryable = true)
}

private fun errorModel(message: UiText, isRetryable: Boolean) =
    ErrorUIModel(message = message, isRetryable = isRetryable)
