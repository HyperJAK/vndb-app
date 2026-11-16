package com.zc.vndb.feature.detail.data.repository

import com.zc.vndb.common.data.util.safeApiCall
import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.detail.data.mapper.charactersQuery
import com.zc.vndb.feature.detail.data.mapper.editionsQuery
import com.zc.vndb.feature.detail.data.mapper.novelQuery
import com.zc.vndb.feature.detail.data.mapper.toDomain
import com.zc.vndb.feature.detail.data.remote.DetailService
import com.zc.vndb.feature.detail.domain.model.Edition
import com.zc.vndb.feature.detail.domain.model.NovelCharacter
import com.zc.vndb.feature.detail.domain.model.NovelDetail
import com.zc.vndb.feature.detail.domain.repository.DetailRepository

class DetailRepositoryImpl(private val service: DetailService) : DetailRepository {

    override suspend fun getNovel(id: String): Result<NovelDetail> = safeApiCall {
        val novel = service.queryNovel(
            novelQuery(id),
        ).results.firstOrNull() ?: throw DomainError.NotFound
        novel.toDomain()
    }

    override suspend fun getCharacters(novelId: String): Result<List<NovelCharacter>> =
        safeApiCall {
            service.queryCharacters(
                charactersQuery(novelId),
            ).results.mapNotNull { it.toDomain(novelId) }
        }

    override suspend fun getEditions(novelId: String): Result<List<Edition>> = safeApiCall {
        service.queryEditions(editionsQuery(novelId)).results.flatMap { it.toDomain() }
    }
}
