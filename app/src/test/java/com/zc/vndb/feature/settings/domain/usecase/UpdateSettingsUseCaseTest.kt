package com.zc.vndb.feature.settings.domain.usecase

import com.zc.vndb.common.domain.model.AppSettings
import com.zc.vndb.common.domain.repository.FakeSettingsRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class UpdateSettingsUseCaseTest {

    private val repository = FakeSettingsRepository()
    private val useCase = UpdateSettingsUseCase(repository)

    @Test
    fun `applies the change`() = runTest {
        useCase { it.copy(showSensitiveContent = true) }

        assertTrue(repository.current.value.showSensitiveContent)
    }

    @Test
    fun `New & Trending keeps at least one language`() = runTest {
        useCase { it.copy(trendingLanguages = emptyList()) }

        assertEquals(
            AppSettings.DEFAULT_TRENDING_LANGUAGES,
            repository.current.value.trendingLanguages,
        )
    }
}
