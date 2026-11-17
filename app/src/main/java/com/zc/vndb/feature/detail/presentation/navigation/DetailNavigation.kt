package com.zc.vndb.feature.detail.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.zc.vndb.feature.detail.presentation.DetailRoute

fun EntryProviderScope<NavKey>.detailEntry(actions: DetailNavActions) {
    entry<DetailNavKey> { key -> DetailRoute(novelId = key.id, actions = actions) }
}
