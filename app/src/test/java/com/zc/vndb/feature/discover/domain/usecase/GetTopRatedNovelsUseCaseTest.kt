package com.zc.vndb.feature.discover.domain.usecase

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.discover.domain.model.DiscoverSamples.novel
import com.zc.vndb.feature.discover.domain.repository.FakeDiscoverRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class GetTopRatedNovelsUseCaseTest {

    private val repository = FakeDiscoverRepository()
    private val useCase = GetTopRatedNovelsUseCase(repository)

    @Test
    fun `returns the pool from the repository`() = runTest {
        repository.topRated = listOf(novel("v1"), novel("v2"))

        assertEquals(listOf(novel("v1"), novel("v2")), useCase().getOrThrow())
        assertEquals(GetTopRatedNovelsUseCase.POOL_SIZE, repository.requestedTopRatedLimit)
    }

    @Test
    fun `propagates repository errors`() = runTest {
        repository.topRatedError = DomainError.RateLimited

        assertEquals(DomainError.RateLimited, useCase().exceptionOrNull())
    }
}
