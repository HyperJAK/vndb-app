package com.zc.vndb.feature.browse.data.repository

import com.zc.vndb.common.data.util.safeApiCall
import com.zc.vndb.feature.browse.data.mapper.toDomain
import com.zc.vndb.feature.browse.data.mapper.toQueryDTO
import com.zc.vndb.feature.browse.data.remote.BrowseService
import com.zc.vndb.feature.browse.domain.model.BrowseFilter
import com.zc.vndb.feature.browse.domain.model.BrowsePage
import com.zc.vndb.feature.browse.domain.repository.BrowseRepository

class BrowseRepositoryImpl(private val service: BrowseService) : BrowseRepository {

    override suspend fun searchNovels(
        filter: BrowseFilter,
        page: Int,
        pageSize: Int,
        withCount: Boolean,
    ): Result<BrowsePage> = safeApiCall {
        service.queryNovels(filter.toQueryDTO(page, pageSize, withCount)).toDomain()
    }
}
