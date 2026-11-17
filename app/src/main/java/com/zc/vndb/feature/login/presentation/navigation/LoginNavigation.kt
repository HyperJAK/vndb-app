package com.zc.vndb.feature.login.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.zc.vndb.feature.login.presentation.LoginRoute

fun EntryProviderScope<NavKey>.loginEntry(onNavigateBack: () -> Unit) {
    entry<LoginNavKey> { LoginRoute(onNavigateBack = onNavigateBack) }
}
