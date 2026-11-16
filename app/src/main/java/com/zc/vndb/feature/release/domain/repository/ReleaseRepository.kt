package com.zc.vndb.feature.release.domain.repository

import com.zc.vndb.feature.release.domain.model.ReleaseDetail

interface ReleaseRepository {
    suspend fun getRelease(id: String): Result<ReleaseDetail>
}
