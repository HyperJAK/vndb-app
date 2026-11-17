package com.zc.vndb.feature.browse.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.zc.vndb.common.ui.navigation.Navigator
import com.zc.vndb.feature.browse.domain.model.BrowseScope
import com.zc.vndb.feature.browse.presentation.BrowseRoute

/** The Browse tab. */
fun EntryProviderScope<NavKey>.browseEntry(onOpenNovel: (id: String) -> Unit) {
    entry<BrowseNavKey> { BrowseRoute(onOpenNovel = onOpenNovel) }
}

/** Browse pages scoped to a tag, a producer or a staff member. */
fun EntryProviderScope<NavKey>.browseScopedEntries(
    onNavigateBack: () -> Unit,
    onOpenNovel: (id: String) -> Unit,
) {
    scopedBrowse<TagNavKey>(onNavigateBack, onOpenNovel) { BrowseScope.Tag(it.id) }
    scopedBrowse<ProducerNavKey>(onNavigateBack, onOpenNovel) { BrowseScope.Producer(it.id) }
    scopedBrowse<StaffNavKey>(onNavigateBack, onOpenNovel) { BrowseScope.Staff(it.id) }
}

private inline fun <reified K : NavKey> EntryProviderScope<NavKey>.scopedBrowse(
    noinline onNavigateBack: () -> Unit,
    noinline onOpenNovel: (id: String) -> Unit,
    crossinline scope: (K) -> BrowseScope,
) {
    entry<K> { key ->
        BrowseRoute(
            scope = scope(key),
            onNavigateBack = onNavigateBack,
            onOpenNovel = onOpenNovel,
        )
    }
}

/** Switches to the Browse tab. */
fun Navigator.navigateToBrowse() = navigate(BrowseNavKey)

/** Opens Browse scoped to a tag. */
fun Navigator.navigateToTag(id: String) = navigate(TagNavKey(id))

/** Opens Browse scoped to a producer. */
fun Navigator.navigateToProducer(id: String) = navigate(ProducerNavKey(id))

/** Opens Browse scoped to a staff member. */
fun Navigator.navigateToStaff(id: String) = navigate(StaffNavKey(id))
