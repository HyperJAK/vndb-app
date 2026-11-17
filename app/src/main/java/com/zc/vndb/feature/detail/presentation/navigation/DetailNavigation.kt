package com.zc.vndb.feature.detail.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.zc.vndb.common.ui.navigation.Navigator
import com.zc.vndb.feature.detail.presentation.DetailRoute

fun EntryProviderScope<NavKey>.detailEntry(actions: DetailNavActions) {
    entry<DetailNavKey> { key -> DetailRoute(novelId = key.id, actions = actions) }
}

/** Opens a visual novel's detail page. */
fun Navigator.navigateToDetail(id: String) = navigate(DetailNavKey(id))
