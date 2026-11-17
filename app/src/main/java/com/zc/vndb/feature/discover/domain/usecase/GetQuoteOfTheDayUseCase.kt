package com.zc.vndb.feature.discover.domain.usecase

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.discover.domain.model.QuoteOfTheDay
import com.zc.vndb.feature.discover.domain.repository.DiscoverRepository
import kotlin.random.Random
import org.koin.core.annotation.Factory

/**
 * One of the best-scored all-ages quotes, picked at random: a single request per call, and a
 * different quote each time. VNDB's `random` filter cannot be limited to all-ages novels.
 */
@Factory
class GetQuoteOfTheDayUseCase(
    private val repository: DiscoverRepository,
    private val random: Random,
) {
    suspend operator fun invoke(): Result<QuoteOfTheDay> =
        repository.getTopQuotes(POOL_SIZE).mapCatching { pool ->
            if (pool.isEmpty()) throw DomainError.NotFound
            pool[random.nextInt(pool.size)]
        }

    companion object {
        /** VNDB's largest page. */
        const val POOL_SIZE = 100
    }
}
