package com.zc.vndb.feature.theme.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.zc.vndb.feature.theme.presentation.ThemeRoute

fun EntryProviderScope<NavKey>.themeEntry(onNavigateBack: () -> Unit) {
    entry<ThemeNavKey> { ThemeRoute(onNavigateBack = onNavigateBack) }
}
