package com.zc.vndb.feature.release.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.zc.vndb.feature.release.presentation.ReleaseRoute

fun EntryProviderScope<NavKey>.releaseEntry(
    onNavigateBack: () -> Unit,
    onOpenNovel: (id: String) -> Unit,
    onOpenProducer: (id: String) -> Unit,
) {
    entry<ReleaseNavKey> { key ->
        ReleaseRoute(
            releaseId = key.id,
            onNavigateBack = onNavigateBack,
            onOpenNovel = onOpenNovel,
            onOpenProducer = onOpenProducer,
        )
    }
}
