package com.zc.vndb.feature.discover.domain.usecase

import com.zc.vndb.feature.discover.domain.model.Release
import com.zc.vndb.feature.discover.domain.model.ReleaseKind
import com.zc.vndb.feature.discover.domain.model.ReleaseNovel
import com.zc.vndb.feature.discover.domain.model.TrendingRelease
import com.zc.vndb.feature.discover.domain.repository.DiscoverRepository
import org.koin.core.annotation.Factory

/**
 * "New & Trending": the newest releases in the user's [languages], one row per visual novel,
 * each labelled with why it is new. Machine-translation-only releases are left out.
 */
@Factory
class GetTrendingReleasesUseCase(private val repository: DiscoverRepository) {
    suspend operator fun invoke(languages: List<String>): Result<List<TrendingRelease>> =
        repository.getRecentReleases(languages, FETCH_LIMIT).map { releases ->
            releases
                .filterNot { it.isMachineTranslatedOnly }
                .mapNotNull { it.toTrendingOrNull() }
                .distinctBy { it.novelId }
                .take(RESULT_LIMIT)
        }

    private val Release.isMachineTranslatedOnly: Boolean
        get() = languages.isNotEmpty() && languages.all { it.isMachineTranslated }

    private fun Release.toTrendingOrNull(): TrendingRelease? {
        val novel = novels.firstOrNull() ?: return null
        return TrendingRelease(
            releaseId = id,
            novelId = novel.id,
            title = novel.title,
            originalTitle = novel.originalTitle,
            kind = kindFor(novel),
            releaseDate = releaseDate,
            platforms = platforms,
            rating = novel.rating,
            cover = novel.cover,
        )
    }

    private fun Release.kindFor(novel: ReleaseNovel): ReleaseKind = when {
        isPatch -> ReleaseKind.RecentPatch

        novel.releaseDate >= releaseDate -> ReleaseKind.NewRelease

        languages.any {
            !it.isMachineTranslated && it.code != novel.originalLanguage
        } -> ReleaseKind.NewTranslation

        else -> ReleaseKind.ReRelease
    }

    companion object {
        const val RESULT_LIMIT = 10

        // Over-fetch so de-duplicating by visual novel still leaves RESULT_LIMIT rows.
        const val FETCH_LIMIT = 30
    }
}
