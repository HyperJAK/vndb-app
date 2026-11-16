package com.zc.vndb.feature.detail.domain.usecase

import com.zc.vndb.feature.detail.domain.model.NovelDetail
import com.zc.vndb.feature.detail.domain.model.RelatedNovel
import com.zc.vndb.feature.detail.domain.repository.DetailRepository
import org.koin.core.annotation.Factory

/** The VN itself. Relations are ordered official first, then oldest first. */
@Factory
class GetNovelDetailUseCase(private val repository: DetailRepository) {

    suspend operator fun invoke(id: String): Result<NovelDetail> = repository.getNovel(
        id,
    ).map { novel ->
        novel.copy(
            relations = novel.relations
                .sortedWith(
                    compareByDescending<RelatedNovel> { it.isOfficial }.thenBy { it.releaseDate },
                )
                .take(MAX_RELATIONS),
            links = novel.links.distinctBy { it.label }.take(MAX_LINKS),
        )
    }

    private companion object {
        const val MAX_RELATIONS = 10
        const val MAX_LINKS = 3
    }
}
