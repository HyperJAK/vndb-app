package com.zc.vndb.feature.detail.domain.usecase

import com.zc.vndb.feature.detail.domain.model.AgeRange
import com.zc.vndb.feature.detail.domain.model.NovelEditions
import com.zc.vndb.feature.detail.domain.repository.DetailRepository

/**
 * A short edition list: the earliest official, non-patch release on each platform (up to 6),
 * plus the age-rating range across all official releases.
 */
class GetNovelEditionsUseCase(private val repository: DetailRepository) {

    suspend operator fun invoke(novelId: String): Result<NovelEditions> =
        repository.getEditions(novelId).map { all ->
            val official = all.filter { it.isOfficial && !it.isPatch }
            val ages = official.mapNotNull { it.minAge }
            NovelEditions(
                editions = official
                    .sortedBy { it.releaseDate }
                    .distinctBy { it.platform }
                    .take(MAX_EDITIONS),
                ageRange = if (ages.isEmpty()) {
                    null
                } else {
                    AgeRange(
                        min = ages.min(),
                        max = ages.max(),
                    )
                },
            )
        }

    private companion object {
        const val MAX_EDITIONS = 6
    }
}
