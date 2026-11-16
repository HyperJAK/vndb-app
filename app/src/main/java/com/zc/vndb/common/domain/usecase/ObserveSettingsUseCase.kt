package com.zc.vndb.common.domain.usecase

import com.zc.vndb.common.domain.model.AppSettings
import com.zc.vndb.common.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

/** The user's settings, for every feature that adapts to them. */
@Factory
class ObserveSettingsUseCase(private val repository: SettingsRepository) {

    operator fun invoke(): Flow<AppSettings> = repository.settings.distinctUntilChanged()

    /** The settings right now, for screens that apply them when they load. */
    suspend fun current(): AppSettings = repository.settings.first()
}
