package com.zc.vndb.feature.discover.domain.usecase

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.discover.domain.repository.FakeDiscoverRepository
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class RollRandomNovelUseCaseTest {

    private val repository = FakeDiscoverRepository()
    private val useCase = RollRandomNovelUseCase(repository, Random(seed = 42))

    @Test
    fun `rolls a number within the id range and returns the next existing novel`() = runTest {
        repository.latestNovelNumber = 100
        repository.novelNumbers = (2..100 step 2).toList()

        repeat(20) {
            val id = useCase().getOrThrow()
            val rolled = repository.requestedNovelNumbers.last()
            assertTrue(rolled in 1..100)
            assertEquals("v${if (rolled % 2 == 0) rolled else rolled + 1}", id)
        }
    }

    @Test
    fun `a single novel is always the roll`() = runTest {
        repository.latestNovelNumber = 1
        repository.novelNumbers = listOf(1)

        assertEquals("v1", useCase().getOrThrow())
    }

    @Test
    fun `no novel at or after the roll is NotFound`() = runTest {
        repository.latestNovelNumber = 10
        repository.novelNumbers = emptyList()

        assertEquals(DomainError.NotFound, useCase().exceptionOrNull())
    }

    @Test
    fun `an empty database is NotFound without a second request`() = runTest {
        repository.latestNovelNumber = 0

        assertEquals(DomainError.NotFound, useCase().exceptionOrNull())
        assertTrue(repository.requestedNovelNumbers.isEmpty())
    }

    @Test
    fun `propagates errors from the latest id lookup`() = runTest {
        repository.latestError = DomainError.Timeout

        assertEquals(DomainError.Timeout, useCase().exceptionOrNull())
    }
}
