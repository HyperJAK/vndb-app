package com.zc.vndb.common.data.repository

import com.zc.vndb.common.data.local.SettingsLocalDataSource
import com.zc.vndb.common.domain.model.AppSettings
import com.zc.vndb.common.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class SettingsRepositoryImpl(private val local: SettingsLocalDataSource) : SettingsRepository {

    override val settings: Flow<AppSettings> = local.settings

    override suspend fun update(transform: (AppSettings) -> AppSettings) = local.update(transform)
}
