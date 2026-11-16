package com.zc.vndb.feature.discover.domain.usecase

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.discover.domain.repository.DiscoverRepository
import kotlin.random.Random
import org.koin.core.annotation.Factory

/**
 * "Random Roll": returns the id of a random visual novel. Picks a random number up to the
 * latest id, then takes the first existing VN at or after it, since VNDB ids have gaps.
 */
@Factory
class RollRandomNovelUseCase(
    private val repository: DiscoverRepository,
    private val random: Random,
) {
    suspend operator fun invoke(): Result<String> = repository.getLatestNovelNumber().fold(
        onSuccess = { latest ->
            if (latest < 1) {
                Result.failure(
                    DomainError.NotFound,
                )
            } else {
                rollUpTo(latest)
            }
        },
        onFailure = { Result.failure(it) },
    )

    private suspend fun rollUpTo(latest: Int): Result<String> {
        val number = random.nextInt(from = 1, until = latest + 1)
        return repository.getFirstNovelIdFrom(number).fold(
            onSuccess = { id ->
                id?.let {
                    Result.success(
                        it,
                    )
                } ?: Result.failure(DomainError.NotFound)
            },
            onFailure = { Result.failure(it) },
        )
    }
}
