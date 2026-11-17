package com.zc.vndb.app

import com.zc.vndb.common.domain.model.AppSettings
import com.zc.vndb.common.domain.model.AppTheme
import com.zc.vndb.common.domain.repository.FakeSettingsRepository
import com.zc.vndb.common.domain.usecase.ObserveSettingsUseCase
import com.zc.vndb.common.util.MainDispatcherRule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule

class AppViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `the theme follows the stored settings`() = runTest {
        val settings = FakeSettingsRepository(AppSettings(theme = AppTheme.AngelicSerenade))
        val viewModel = AppViewModel(ObserveSettingsUseCase(settings))
        advanceUntilIdle()

        assertEquals(AppTheme.AngelicSerenade, viewModel.theme.value)

        settings.update { it.copy(theme = AppTheme.Original) }
        advanceUntilIdle()

        assertEquals(AppTheme.Original, viewModel.theme.value)
    }
}
