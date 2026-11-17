package com.zc.vndb.feature.browse.domain.usecase

import com.zc.vndb.feature.browse.domain.model.ProducerInfo
import com.zc.vndb.feature.browse.domain.repository.BrowseRepository
import org.koin.core.annotation.Factory

@Factory
class GetProducerUseCase(private val repository: BrowseRepository) {
    suspend operator fun invoke(id: String): Result<ProducerInfo> = repository.getProducer(id)
}
