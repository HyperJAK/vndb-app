package com.zc.vndb.feature.theme.presentation

import app.cash.turbine.test
import com.zc.vndb.common.domain.model.AppSettings
import com.zc.vndb.common.domain.model.AppTheme
import com.zc.vndb.common.domain.repository.FakeSettingsRepository
import com.zc.vndb.common.domain.usecase.ObserveSettingsUseCase
import com.zc.vndb.common.util.MainDispatcherRule
import com.zc.vndb.feature.theme.domain.usecase.SelectThemeUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule

class ThemeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val settings = FakeSettingsRepository()

    private fun createViewModel() = ThemeViewModel(
        observeSettings = ObserveSettingsUseCase(settings),
        selectTheme = SelectThemeUseCase(settings),
    )

    private fun TestScope.collect(state: StateFlow<ThemeState>) {
        backgroundScope.launch { state.collect {} }
    }

    private fun ThemeState.selected() = themes.filter { it.isSelected }.map { it.theme }

    @Test
    fun `lists every theme with Original selected by default`() = runTest {
        val viewModel = createViewModel()
        collect(viewModel.state)
        advanceUntilIdle()

        assertEquals(AppTheme.entries, viewModel.state.value.themes.map { it.theme })
        assertEquals(listOf(AppTheme.Original), viewModel.state.value.selected())
    }

    @Test
    fun `selecting a theme saves it and moves the selection`() = runTest {
        val viewModel = createViewModel()
        collect(viewModel.state)

        viewModel.onIntent(ThemeIntent.ThemeSelected(AppTheme.AngelicSerenade))
        advanceUntilIdle()

        assertEquals(AppTheme.AngelicSerenade, settings.current.value.theme)
        assertEquals(listOf(AppTheme.AngelicSerenade), viewModel.state.value.selected())
    }

    @Test
    fun `starts on the stored theme`() = runTest {
        settings.current.value = AppSettings(theme = AppTheme.AngelicSerenade)
        val viewModel = createViewModel()
        collect(viewModel.state)
        advanceUntilIdle()

        assertEquals(listOf(AppTheme.AngelicSerenade), viewModel.state.value.selected())
    }

    @Test
    fun `back navigates back`() = runTest {
        val viewModel = createViewModel()

        viewModel.effect.test {
            viewModel.onIntent(ThemeIntent.BackClicked)
            assertEquals(ThemeEffect.NavigateBack, awaitItem())
        }
    }
}
