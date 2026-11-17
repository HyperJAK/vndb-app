package com.zc.vndb.feature.theme.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zc.vndb.common.domain.usecase.ObserveSettingsUseCase
import com.zc.vndb.feature.theme.domain.usecase.SelectThemeUseCase
import com.zc.vndb.feature.theme.presentation.mapper.themeState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

/** A theme is saved as soon as it is picked; the app root re-themes from the stored value. */
@KoinViewModel
class ThemeViewModel(
    observeSettings: ObserveSettingsUseCase,
    private val selectTheme: SelectThemeUseCase,
) : ViewModel() {

    val state: StateFlow<ThemeState> = observeSettings()
        .map { it.theme }
        .distinctUntilChanged()
        .map(::themeState)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), ThemeState())

    private val _effect = Channel<ThemeEffect>(Channel.BUFFERED)
    val effect: Flow<ThemeEffect> = _effect.receiveAsFlow()

    fun onIntent(intent: ThemeIntent) {
        when (intent) {
            ThemeIntent.BackClicked -> _effect.trySend(ThemeEffect.NavigateBack)

            is ThemeIntent.ThemeSelected -> viewModelScope.launch {
                selectTheme(intent.theme)
            }
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
