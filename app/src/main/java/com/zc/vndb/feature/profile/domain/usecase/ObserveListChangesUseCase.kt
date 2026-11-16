package com.zc.vndb.feature.profile.domain.usecase

import com.zc.vndb.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

/** Emits a VN id each time its list entry is saved or removed in the app. */
@Factory
class ObserveListChangesUseCase(private val repository: ProfileRepository) {

    operator fun invoke(): Flow<String> = repository.listChanges
}
