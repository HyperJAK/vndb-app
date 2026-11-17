package com.zc.vndb.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zc.vndb.common.domain.model.AppSettings
import com.zc.vndb.common.domain.model.AppTheme
import com.zc.vndb.common.domain.usecase.ObserveSettingsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.KoinViewModel

/** App-wide settings that the whole UI reads: [AppRoute] provides them and applies the theme. */
@KoinViewModel
class AppViewModel(observeSettings: ObserveSettingsUseCase) : ViewModel() {

    val showSensitiveContent: StateFlow<Boolean> = observeSettings()
        .map { it.showSensitiveContent }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    /** The chosen theme; null until the stored settings are read, so no wrong theme shows at launch. */
    val theme: StateFlow<AppTheme?> = observeSettings()
        .map<AppSettings, AppTheme?> { it.theme }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}
