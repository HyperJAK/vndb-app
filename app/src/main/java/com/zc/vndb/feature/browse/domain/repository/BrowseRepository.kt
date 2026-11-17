package com.zc.vndb.feature.browse.domain.repository

import com.zc.vndb.feature.browse.domain.model.BrowseFilter
import com.zc.vndb.feature.browse.domain.model.BrowsePage
import com.zc.vndb.feature.browse.domain.model.ProducerInfo
import com.zc.vndb.feature.browse.domain.model.TagInfo

interface BrowseRepository {

    suspend fun getTag(id: String): Result<TagInfo>

    suspend fun getProducer(id: String): Result<ProducerInfo>

    /** One page (1-based) of novels matching [filter]; [withCount] also asks for the total. */
    suspend fun searchNovels(
        filter: BrowseFilter,
        page: Int,
        pageSize: Int,
        withCount: Boolean,
    ): Result<BrowsePage>
}
