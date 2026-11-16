package com.zc.vndb.feature.profile.domain.usecase

import com.zc.vndb.feature.profile.domain.model.EntryPage
import com.zc.vndb.feature.profile.domain.repository.ProfileRepository

class GetListEntriesUseCase(private val repository: ProfileRepository) {

    suspend operator fun invoke(userId: String, labelId: Int, page: Int): Result<EntryPage> =
        repository.getEntries(userId, labelId, page, PAGE_SIZE)

    companion object {
        const val FIRST_PAGE = 1
        const val PAGE_SIZE = 25
    }
}
