package com.zc.vndb.feature.settings.domain.usecase

import com.zc.vndb.common.domain.model.AppSettings
import com.zc.vndb.common.domain.repository.SettingsRepository
import org.koin.core.annotation.Factory

/** Changes settings; New & Trending keeps at least one language. */
@Factory
class UpdateSettingsUseCase(private val repository: SettingsRepository) {

    suspend operator fun invoke(transform: (AppSettings) -> AppSettings) =
        repository.update { current ->
            transform(current).let { updated ->
                if (updated.trendingLanguages.isEmpty()) {
                    updated.copy(
                        trendingLanguages = current.trendingLanguages,
                    )
                } else {
                    updated
                }
            }
        }
}
