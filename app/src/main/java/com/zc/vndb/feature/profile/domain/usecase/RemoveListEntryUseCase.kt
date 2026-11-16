package com.zc.vndb.feature.profile.domain.usecase

import com.zc.vndb.feature.profile.domain.repository.ProfileRepository
import org.koin.core.annotation.Factory

/** Removes a VN, its vote, dates, notes and releases from the signed-in user's list. */
@Factory
class RemoveListEntryUseCase(private val repository: ProfileRepository) {

    suspend operator fun invoke(novelId: String): Result<Unit> = repository.removeEntry(novelId)
}
