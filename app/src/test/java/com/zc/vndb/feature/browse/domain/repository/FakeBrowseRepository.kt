package com.zc.vndb.feature.browse.domain.repository

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.browse.domain.model.BrowseFilter
import com.zc.vndb.feature.browse.domain.model.BrowseNovel
import com.zc.vndb.feature.browse.domain.model.BrowsePage

/** Serves [novels] in pages; set [error] (or [errorOnPage]) to fail. Records every request. */
class FakeBrowseRepository : BrowseRepository {

    data class Request(
        val filter: BrowseFilter,
        val page: Int,
        val pageSize: Int,
        val withCount: Boolean,
    )

    var novels: List<BrowseNovel> = emptyList()
    var error: DomainError? = null
    var errorOnPage: Int? = null
    val requests = mutableListOf<Request>()

    override suspend fun searchNovels(
        filter: BrowseFilter,
        page: Int,
        pageSize: Int,
        withCount: Boolean,
    ): Result<BrowsePage> {
        requests += Request(filter, page, pageSize, withCount)
        val failure = error ?: DomainError.Server.takeIf { errorOnPage == page }
        if (failure != null) return Result.failure(failure)
        val slice = novels.drop((page - 1) * pageSize).take(pageSize)
        return Result.success(
            BrowsePage(
                novels = slice,
                totalCount = if (withCount) novels.size else null,
                hasMore = page * pageSize < novels.size,
            ),
        )
    }
}
