package com.zc.vndb.feature.discover.domain.usecase

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.discover.domain.model.DiscoverSamples
import com.zc.vndb.feature.discover.domain.repository.FakeDiscoverRepository
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class GetQuoteOfTheDayUseCaseTest {

    private val repository = FakeDiscoverRepository().apply {
        quotes = (1..10).map { DiscoverSamples.quote(id = "q$it") }
    }

    @Test
    fun `asks for one pool of quotes and picks from it`() = runTest {
        val quote = GetQuoteOfTheDayUseCase(repository, Random(seed = 1)).invoke().getOrThrow()

        assertEquals(1, repository.quoteCalls)
        assertEquals(GetQuoteOfTheDayUseCase.POOL_SIZE, repository.requestedQuoteLimit)
        assertTrue(quote in repository.quotes)
    }

    @Test
    fun `the pick is random, so later calls can differ`() = runTest {
        val useCase = GetQuoteOfTheDayUseCase(repository, Random(seed = 7))

        val picks = (1..20).map { useCase().getOrThrow().id }.toSet()

        assertTrue(picks.size > 1)
    }

    @Test
    fun `an empty pool is NotFound`() = runTest {
        repository.quotes = emptyList()

        assertEquals(
            DomainError.NotFound,
            GetQuoteOfTheDayUseCase(repository, Random(seed = 1)).invoke().exceptionOrNull(),
        )
    }

    @Test
    fun `a failed request is passed on`() = runTest {
        repository.quoteError = DomainError.Network

        assertEquals(
            DomainError.Network,
            GetQuoteOfTheDayUseCase(repository, Random(seed = 1)).invoke().exceptionOrNull(),
        )
    }
}
