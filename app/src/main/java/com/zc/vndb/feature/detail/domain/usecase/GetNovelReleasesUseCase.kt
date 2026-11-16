package com.zc.vndb.feature.detail.domain.usecase

import com.zc.vndb.feature.detail.domain.model.ReleaseSummary
import com.zc.vndb.feature.detail.domain.model.ResultPage
import com.zc.vndb.feature.detail.domain.repository.DetailRepository
import org.koin.core.annotation.Factory

@Factory
class GetNovelReleasesUseCase(private val repository: DetailRepository) {

    suspend operator fun invoke(novelId: String, page: Int): Result<ResultPage<ReleaseSummary>> =
        repository.getReleases(novelId, page, PAGE_SIZE)

    companion object {
        const val PAGE_SIZE = 50
    }
}
