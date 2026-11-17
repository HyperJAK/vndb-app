package com.zc.vndb.feature.theme.domain.usecase

import com.zc.vndb.common.domain.model.AppTheme
import com.zc.vndb.common.domain.repository.SettingsRepository
import org.koin.core.annotation.Factory

/** Saves the chosen theme; the app root re-themes from the stored value. */
@Factory
class SelectThemeUseCase(private val repository: SettingsRepository) {

    suspend operator fun invoke(theme: AppTheme) = repository.update { it.copy(theme = theme) }
}
