package com.zc.vndb.feature.release.domain.usecase

import com.zc.vndb.feature.release.domain.model.ReleaseDetail
import com.zc.vndb.feature.release.domain.repository.ReleaseRepository
import org.koin.core.annotation.Factory

/**
 * The release page: main language first, package front before other images, VNs oldest first,
 * and one external link per site.
 */
@Factory
class GetReleaseUseCase(private val repository: ReleaseRepository) {

    suspend operator fun invoke(id: String): Result<ReleaseDetail> = repository.getRelease(
        id,
    ).map { release ->
        release.copy(
            languages = release.languages.sortedByDescending { it.isMain },
            images = release.images.sortedBy { it.type.ordinal },
            novels = release.novels.sortedBy { it.releaseDate },
            links = release.links.distinctBy { it.label },
        )
    }
}
