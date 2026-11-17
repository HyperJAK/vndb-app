package com.zc.vndb.feature.settings.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.zc.vndb.common.ui.navigation.Navigator
import com.zc.vndb.feature.settings.presentation.SettingsRoute

fun EntryProviderScope<NavKey>.settingsEntry(onNavigateBack: () -> Unit, onOpenTheme: () -> Unit) {
    entry<SettingsNavKey> {
        SettingsRoute(onNavigateBack = onNavigateBack, onOpenTheme = onOpenTheme)
    }
}

/** Opens Settings. */
fun Navigator.navigateToSettings() = navigate(SettingsNavKey)
