package com.zc.vndb.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zc.vndb.common.domain.usecase.ObserveSettingsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.KoinViewModel

/** App-wide settings that the whole UI reads (provided as composition locals by [AppRoute]). */
@KoinViewModel
class AppViewModel(observeSettings: ObserveSettingsUseCase) : ViewModel() {

    val showSensitiveContent: StateFlow<Boolean> = observeSettings()
        .map { it.showSensitiveContent }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)
}
