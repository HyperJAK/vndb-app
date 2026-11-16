package com.zc.vndb.feature.discover.domain.usecase

import com.zc.vndb.feature.discover.domain.model.DatabaseStats
import com.zc.vndb.feature.discover.domain.repository.DiscoverRepository
import org.koin.core.annotation.Factory

@Factory
class GetDatabaseStatsUseCase(private val repository: DiscoverRepository) {
    suspend operator fun invoke(): Result<DatabaseStats> = repository.getDatabaseStats()
}
