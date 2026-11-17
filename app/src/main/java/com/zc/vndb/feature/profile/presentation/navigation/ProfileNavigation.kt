package com.zc.vndb.feature.profile.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.zc.vndb.feature.profile.presentation.ProfileRoute

fun EntryProviderScope<NavKey>.profileEntry(
    onOpenLogin: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenNovel: (id: String) -> Unit,
    onOpenListEditor: (novelId: String, title: String) -> Unit,
) {
    entry<ProfileNavKey> {
        ProfileRoute(
            onOpenLogin = onOpenLogin,
            onOpenSettings = onOpenSettings,
            onOpenNovel = onOpenNovel,
            onOpenListEditor = onOpenListEditor,
        )
    }
}
