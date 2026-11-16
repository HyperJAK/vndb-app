package com.zc.vndb.feature.detail.domain.usecase

import com.zc.vndb.feature.detail.domain.model.Screenshot
import com.zc.vndb.feature.detail.domain.repository.DetailRepository

/** Screenshots, grouped by release in the order VNDB lists them. */
class GetNovelScreenshotsUseCase(private val repository: DetailRepository) {

    suspend operator fun invoke(novelId: String): Result<List<Screenshot>> =
        repository.getScreenshots(novelId).map { screenshots ->
            val releaseOrder = screenshots.map { it.releaseId }.distinct()
            screenshots.sortedBy { releaseOrder.indexOf(it.releaseId) }
        }
}
