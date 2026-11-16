package com.zc.vndb.feature.discover.data.repository

import com.zc.vndb.common.data.util.safeApiCall
import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.discover.data.mapper.firstNovelFromQuery
import com.zc.vndb.feature.discover.data.mapper.idNumber
import com.zc.vndb.feature.discover.data.mapper.latestNovelQuery
import com.zc.vndb.feature.discover.data.mapper.novelDetailsQuery
import com.zc.vndb.feature.discover.data.mapper.recentReleasesQuery
import com.zc.vndb.feature.discover.data.mapper.toDiscoverNovel
import com.zc.vndb.feature.discover.data.mapper.toDomain
import com.zc.vndb.feature.discover.data.mapper.toNovelDetails
import com.zc.vndb.feature.discover.data.mapper.topRatedNovelsQuery
import com.zc.vndb.feature.discover.data.remote.DiscoverService
import com.zc.vndb.feature.discover.domain.model.DatabaseStats
import com.zc.vndb.feature.discover.domain.model.DiscoverNovel
import com.zc.vndb.feature.discover.domain.model.NovelDetails
import com.zc.vndb.feature.discover.domain.model.Release
import com.zc.vndb.feature.discover.domain.repository.DiscoverRepository
import org.koin.core.annotation.Single

@Single
class DiscoverRepositoryImpl(private val service: DiscoverService) : DiscoverRepository {

    override suspend fun getTopRatedNovels(limit: Int): Result<List<DiscoverNovel>> = safeApiCall {
        service.queryNovels(topRatedNovelsQuery(limit)).results.map { it.toDiscoverNovel() }
    }

    override suspend fun getNovelDetails(id: String): Result<NovelDetails> = safeApiCall {
        val novel = service.queryNovels(
            novelDetailsQuery(id),
        ).results.firstOrNull() ?: throw DomainError.NotFound
        novel.toNovelDetails()
    }

    override suspend fun getRecentReleases(
        languageCodes: List<String>,
        limit: Int,
    ): Result<List<Release>> = safeApiCall {
        service.queryReleases(
            recentReleasesQuery(languageCodes, limit),
        ).results.map { it.toDomain() }
    }

    override suspend fun getDatabaseStats(): Result<DatabaseStats> = safeApiCall {
        service.getStats().toDomain()
    }

    override suspend fun getLatestNovelNumber(): Result<Int> = safeApiCall {
        service.queryNovels(latestNovelQuery()).results.firstOrNull()?.idNumber() ?: 0
    }

    override suspend fun getFirstNovelIdFrom(number: Int): Result<String?> = safeApiCall {
        service.queryNovels(firstNovelFromQuery(number)).results.firstOrNull()?.id
    }
}
