package com.zc.vndb.common.domain.repository

import com.zc.vndb.common.domain.model.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeSettingsRepository(initial: AppSettings = AppSettings()) : SettingsRepository {

    val current = MutableStateFlow(initial)
    override val settings: StateFlow<AppSettings> = current

    override suspend fun update(transform: (AppSettings) -> AppSettings) {
        current.value = transform(current.value)
    }
}
