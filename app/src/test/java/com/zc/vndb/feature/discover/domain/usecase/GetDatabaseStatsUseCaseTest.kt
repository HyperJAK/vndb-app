package com.zc.vndb.feature.discover.domain.usecase

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.discover.domain.model.DatabaseStats
import com.zc.vndb.feature.discover.domain.repository.FakeDiscoverRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class GetDatabaseStatsUseCaseTest {

    private val repository = FakeDiscoverRepository()
    private val useCase = GetDatabaseStatsUseCase(repository)

    @Test
    fun `returns the stats from the repository`() = runTest {
        repository.stats = DatabaseStats(novelCount = 68053)

        assertEquals(DatabaseStats(novelCount = 68053), useCase().getOrThrow())
    }

    @Test
    fun `propagates repository errors`() = runTest {
        repository.statsError = DomainError.Server

        assertEquals(DomainError.Server, useCase().exceptionOrNull())
    }
}
