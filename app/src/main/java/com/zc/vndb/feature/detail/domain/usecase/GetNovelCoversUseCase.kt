package com.zc.vndb.feature.detail.domain.usecase

import com.zc.vndb.feature.detail.domain.model.CoverArt
import com.zc.vndb.feature.detail.domain.repository.DetailRepository

/** Each distinct cover once, package fronts first. */
class GetNovelCoversUseCase(private val repository: DetailRepository) {

    suspend operator fun invoke(novelId: String): Result<List<CoverArt>> =
        repository.getCovers(novelId).map { covers ->
            covers.distinctBy { it.image.url }.sortedByDescending { it.isPackageFront }
        }
}
