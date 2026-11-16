package com.zc.vndb.feature.discover.domain.usecase

import com.zc.vndb.feature.discover.domain.model.DiscoverNovel
import com.zc.vndb.feature.discover.domain.repository.DiscoverRepository

/** The top-rated pool: shown (partly) as the Top Rated row and used to pick the VN of the Week. */
class GetTopRatedNovelsUseCase(private val repository: DiscoverRepository) {
    suspend operator fun invoke(): Result<List<DiscoverNovel>> = repository.getTopRatedNovels(
        POOL_SIZE,
    )

    companion object {
        const val POOL_SIZE = 50
    }
}
