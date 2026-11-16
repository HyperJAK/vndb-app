package com.zc.vndb.common.ui.component.error.mapper

import com.zc.vndb.R
import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.ui.component.error.model.ErrorUIModel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText

/** Maps any failure to the shared error UI. Unknown errors get a generic, retryable message. */
fun Throwable.toErrorUIModel(): ErrorUIModel = when (this) {
    DomainError.Network, DomainError.Timeout -> errorModel(
        R.string.error_network,
        isRetryable = true,
    )

    DomainError.NotFound -> errorModel(R.string.error_not_found, isRetryable = false)

    DomainError.RateLimited -> errorModel(R.string.error_rate_limited, isRetryable = true)

    DomainError.Unauthorized -> errorModel(R.string.error_unauthorized, isRetryable = false)

    DomainError.Server -> errorModel(R.string.error_server, isRetryable = true)

    else -> errorModel(R.string.error_generic, isRetryable = true)
}

private fun errorModel(id: StringResource, isRetryable: Boolean) =
    ErrorUIModel(message = UiText.StringResource(id), isRetryable = isRetryable)
