package com.zc.vndb.feature.settings.domain.usecase

import com.zc.vndb.common.domain.repository.ImageCacheRepository
import org.koin.core.annotation.Factory

@Factory
class GetImageCacheSizeUseCase(private val repository: ImageCacheRepository) {

    /** Bytes the image cache uses on disk. */
    suspend operator fun invoke(): Long = repository.size()
}
