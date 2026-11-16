package com.zc.vndb.feature.detail.domain.usecase

import com.zc.vndb.feature.detail.domain.model.CoverArt
import com.zc.vndb.feature.detail.domain.repository.DetailRepository
import org.koin.core.annotation.Factory

/** Each distinct cover once, package fronts first. */
@Factory
class GetNovelCoversUseCase(private val repository: DetailRepository) {

    suspend operator fun invoke(novelId: String): Result<List<CoverArt>> =
        repository.getCovers(novelId).map { covers ->
            covers.distinctBy { it.image.url }.sortedByDescending { it.isPackageFront }
        }
}
