package com.zc.vndb.feature.browse.domain.usecase

import com.zc.vndb.feature.browse.domain.model.StaffInfo
import com.zc.vndb.feature.browse.domain.repository.BrowseRepository
import org.koin.core.annotation.Factory

@Factory
class GetStaffUseCase(private val repository: BrowseRepository) {
    suspend operator fun invoke(id: String): Result<StaffInfo> = repository.getStaff(id)
}
