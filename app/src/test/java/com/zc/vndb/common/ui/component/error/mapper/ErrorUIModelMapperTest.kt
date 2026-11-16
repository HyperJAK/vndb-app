package com.zc.vndb.common.ui.component.error.mapper

import com.zc.vndb.R
import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.ui.component.error.model.ErrorUIModel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import kotlin.test.Test
import kotlin.test.assertEquals

class ErrorUIModelMapperTest {

    private fun expected(id: StringResource, isRetryable: Boolean) = ErrorUIModel(
        UiText.StringResource(id),
        isRetryable,
    )

    @Test
    fun `connection problems are retryable network errors`() {
        assertEquals(expected(R.string.error_network, true), DomainError.Network.toErrorUIModel())
        assertEquals(expected(R.string.error_network, true), DomainError.Timeout.toErrorUIModel())
    }

    @Test
    fun `not found and unauthorized cannot be retried`() {
        assertEquals(
            expected(R.string.error_not_found, false),
            DomainError.NotFound.toErrorUIModel(),
        )
        assertEquals(
            expected(R.string.error_unauthorized, false),
            DomainError.Unauthorized.toErrorUIModel(),
        )
    }

    @Test
    fun `rate limit and server errors are retryable`() {
        assertEquals(
            expected(R.string.error_rate_limited, true),
            DomainError.RateLimited.toErrorUIModel(),
        )
        assertEquals(expected(R.string.error_server, true), DomainError.Server.toErrorUIModel())
    }

    @Test
    fun `anything else is a generic retryable error`() {
        assertEquals(
            expected(R.string.error_generic, true),
            DomainError.InvalidData.toErrorUIModel(),
        )
        assertEquals(
            expected(R.string.error_generic, true),
            DomainError.Unknown(IllegalStateException()).toErrorUIModel(),
        )
        assertEquals(
            expected(R.string.error_generic, true),
            IllegalArgumentException().toErrorUIModel(),
        )
    }
}
