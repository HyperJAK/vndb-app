package com.zc.vndb.feature.settings.domain.usecase

import com.zc.vndb.common.domain.repository.ImageCacheRepository
import org.koin.core.annotation.Factory

@Factory
class ClearImageCacheUseCase(private val repository: ImageCacheRepository) {

    suspend operator fun invoke() = repository.clear()
}
