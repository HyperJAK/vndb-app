package com.zc.vndb.feature.discover.domain.usecase

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.domain.model.TagCategory
import com.zc.vndb.feature.discover.domain.model.DiscoverNovel
import com.zc.vndb.feature.discover.domain.model.FeaturedNovel
import com.zc.vndb.feature.discover.domain.model.NovelDetails
import com.zc.vndb.feature.discover.domain.model.StaffRole
import com.zc.vndb.feature.discover.domain.repository.DiscoverRepository
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

/**
 * Picks the "VN of the Week" from the top-rated [pool]. The pick depends only on the current
 * week (Monday-based, UTC), so it is the same for every user and changes once a week.
 */
class GetFeaturedNovelUseCase(
    private val repository: DiscoverRepository,
    private val clock: Clock,
) {
    suspend operator fun invoke(pool: List<DiscoverNovel>): Result<FeaturedNovel> {
        if (pool.isEmpty()) return Result.failure(DomainError.NotFound)
        val pick = pool[weekIndex().mod(pool.size)]
        return repository.getNovelDetails(pick.id).map { it.toFeatured() }
    }

    private fun weekIndex(): Long {
        val epochDays = clock.todayIn(TimeZone.UTC).toEpochDays()
        // 1970-01-01 was a Thursday; shifting by 3 days makes weeks start on Monday.
        return (epochDays + EPOCH_TO_MONDAY_OFFSET_DAYS).floorDiv(DAYS_PER_WEEK)
    }

    private fun NovelDetails.toFeatured(): FeaturedNovel = FeaturedNovel(
        id = id,
        title = title,
        rating = rating,
        cover = cover,
        description = description,
        developerName = developerNames.firstOrNull(),
        scenarioWriter = staff.firstOrNull { it.role == StaffRole.Scenario }?.name,
        tags = tags
            .filter { it.category == TagCategory.Content && it.spoilerLevel == 0 }
            .sortedByDescending { it.rating }
            .take(MAX_TAGS)
            .map { it.name },
        isAllAges = cover != null &&
            cover.sexual < ALL_AGES_MAX_SEXUAL &&
            tags.none { it.category == TagCategory.Sexual },
    )

    private companion object {
        const val EPOCH_TO_MONDAY_OFFSET_DAYS = 3L
        const val DAYS_PER_WEEK = 7L
        const val MAX_TAGS = 2
        const val ALL_AGES_MAX_SEXUAL = 0.5
    }
}
