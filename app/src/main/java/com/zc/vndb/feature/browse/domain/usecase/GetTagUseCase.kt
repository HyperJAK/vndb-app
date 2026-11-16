package com.zc.vndb.feature.browse.domain.usecase

import com.zc.vndb.feature.browse.domain.model.TagInfo
import com.zc.vndb.feature.browse.domain.repository.BrowseRepository
import org.koin.core.annotation.Factory

@Factory
class GetTagUseCase(private val repository: BrowseRepository) {
    suspend operator fun invoke(id: String): Result<TagInfo> = repository.getTag(id)
}
