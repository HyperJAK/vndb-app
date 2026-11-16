package com.zc.vndb.feature.detail.domain.repository

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.detail.domain.model.Edition
import com.zc.vndb.feature.detail.domain.model.NovelCharacter
import com.zc.vndb.feature.detail.domain.model.NovelDetail

class FakeDetailRepository : DetailRepository {

    val novels = mutableMapOf<String, NovelDetail>()
    var characters: List<NovelCharacter> = emptyList()
    var editions: List<Edition> = emptyList()
    var charactersError: DomainError? = null
    var editionsError: DomainError? = null

    override suspend fun getNovel(id: String): Result<NovelDetail> =
        novels[id]?.let { Result.success(it) } ?: Result.failure(DomainError.NotFound)

    override suspend fun getCharacters(novelId: String): Result<List<NovelCharacter>> =
        charactersError?.let { Result.failure(it) } ?: Result.success(characters)

    override suspend fun getEditions(novelId: String): Result<List<Edition>> =
        editionsError?.let { Result.failure(it) } ?: Result.success(editions)
}
