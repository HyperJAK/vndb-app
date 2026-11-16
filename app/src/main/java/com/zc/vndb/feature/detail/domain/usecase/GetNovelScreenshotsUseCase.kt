package com.zc.vndb.feature.detail.domain.usecase

import com.zc.vndb.feature.detail.domain.model.Screenshot
import com.zc.vndb.feature.detail.domain.repository.DetailRepository
import org.koin.core.annotation.Factory

/** Screenshots, grouped by release in the order VNDB lists them. */
@Factory
class GetNovelScreenshotsUseCase(private val repository: DetailRepository) {

    suspend operator fun invoke(novelId: String): Result<List<Screenshot>> =
        repository.getScreenshots(novelId).map { screenshots ->
            val releaseOrder = screenshots.map { it.releaseId }.distinct()
            screenshots.sortedBy { releaseOrder.indexOf(it.releaseId) }
        }
}
