package com.zc.vndb.feature.release.domain.repository

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.feature.release.domain.model.ReleaseDetail

class FakeReleaseRepository : ReleaseRepository {

    val releases = mutableMapOf<String, ReleaseDetail>()

    override suspend fun getRelease(id: String): Result<ReleaseDetail> =
        releases[id]?.let { Result.success(it) } ?: Result.failure(DomainError.NotFound)
}
