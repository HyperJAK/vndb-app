package com.zc.vndb.feature.profile.data.repository

import com.zc.vndb.common.data.util.safeApiCall
import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.profile.data.local.SessionLocalDataSource
import com.zc.vndb.feature.profile.data.mapper.entriesQuery
import com.zc.vndb.feature.profile.data.mapper.toDomain
import com.zc.vndb.feature.profile.data.remote.ProfileService
import com.zc.vndb.feature.profile.domain.model.EntryPage
import com.zc.vndb.feature.profile.domain.model.ListLabel
import com.zc.vndb.feature.profile.domain.model.UserProfile
import com.zc.vndb.feature.profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val service: ProfileService,
    private val local: SessionLocalDataSource,
) : ProfileRepository {

    override suspend fun getUser(userId: String): Result<UserProfile> = safeApiCall {
        service.user(userId)[userId]?.toDomain() ?: throw DomainError.NotFound
    }

    override suspend fun getLabels(userId: String): Result<List<ListLabel>> = authorized { token ->
        service.labels(userId, token).labels.map { it.toDomain() }
    }

    override suspend fun getEntries(
        userId: String,
        labelId: Int,
        page: Int,
        pageSize: Int,
    ): Result<EntryPage> = authorized { token ->
        val result = service.entries(entriesQuery(userId, labelId, page, pageSize), token)
        EntryPage(entries = result.results.map { it.toDomain() }, hasMore = result.more)
    }

    /** Runs [block] with the stored token; a rejected token signs the user out. */
    private suspend fun <T> authorized(block: suspend (token: String?) -> T): Result<T> {
        val token = local.token()
        return safeApiCall { block(token) }.onFailure { error ->
            if (error == DomainError.Unauthorized && token != null) local.clear()
        }
    }
}
