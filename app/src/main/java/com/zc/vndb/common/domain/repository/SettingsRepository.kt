package com.zc.vndb.common.domain.repository

import com.zc.vndb.common.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    /** Current settings; emits again whenever one changes. */
    val settings: Flow<AppSettings>

    /** Applies [transform] to the stored settings in one write. */
    suspend fun update(transform: (AppSettings) -> AppSettings)
}
