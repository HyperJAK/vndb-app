package com.zc.vndb.app.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import com.zc.vndb.common.ui.navigation.BottomSheetSceneStrategy
import com.zc.vndb.common.ui.navigation.Navigator
import com.zc.vndb.feature.browse.domain.model.BrowseScope
import com.zc.vndb.feature.browse.presentation.BrowseRoute
import com.zc.vndb.feature.character.presentation.CharacterRoute
import com.zc.vndb.feature.detail.presentation.DetailRoute
import com.zc.vndb.feature.discover.presentation.DiscoverRoute
import com.zc.vndb.feature.listeditor.presentation.ListEditorRoute
import com.zc.vndb.feature.login.presentation.LoginRoute
import com.zc.vndb.feature.profile.presentation.ProfileRoute
import com.zc.vndb.feature.release.presentation.ReleaseRoute
import com.zc.vndb.feature.settings.presentation.SettingsRoute
import com.zc.vndb.feature.theme.presentation.ThemeRoute

internal fun appEntryProvider(navigator: Navigator) = entryProvider<NavKey> {
    entry<DiscoverNavKey> {
        DiscoverRoute(
            onOpenNovel = { id -> navigator.navigate(NovelDetailNavKey(id)) },
            onOpenBrowse = { navigator.navigate(BrowseNavKey) },
        )
    }
    entry<BrowseNavKey> {
        BrowseRoute(onOpenNovel = { id -> navigator.navigate(NovelDetailNavKey(id)) })
    }
    entry<ProfileNavKey> {
        ProfileRoute(
            onOpenLogin = { navigator.navigate(LoginNavKey) },
            onOpenSettings = { navigator.navigate(SettingsNavKey) },
            onOpenNovel = { id -> navigator.navigate(NovelDetailNavKey(id)) },
            onOpenListEditor = { id, title -> navigator.navigate(ListEditorNavKey(id, title)) },
        )
    }
    entry<SettingsNavKey> {
        SettingsRoute(
            onNavigateBack = navigator::goBack,
            onOpenTheme = { navigator.navigate(ThemeNavKey) },
        )
    }
    entry<ThemeNavKey> {
        ThemeRoute(onNavigateBack = navigator::goBack)
    }
    entry<LoginNavKey> {
        LoginRoute(onNavigateBack = navigator::goBack)
    }
    entry<NovelDetailNavKey> { key ->
        DetailRoute(
            novelId = key.id,
            onNavigateBack = { navigator.goBack() },
            onOpenNovel = { id -> navigator.navigate(NovelDetailNavKey(id)) },
            onOpenCharacter = { id -> navigator.navigate(CharacterNavKey(id)) },
            onOpenTag = { id -> navigator.navigate(TagNavKey(id)) },
            onOpenProducer = { id -> navigator.navigate(ProducerNavKey(id)) },
            onOpenStaff = { id -> navigator.navigate(StaffNavKey(id)) },
            onOpenRelease = { id -> navigator.navigate(ReleaseNavKey(id)) },
            onOpenListEditor = { id, title -> navigator.navigate(ListEditorNavKey(id, title)) },
        )
    }
    pageEntries(navigator)
}

/** Pages opened from a VN or a tab, and the list editor sheet. */
@OptIn(ExperimentalMaterial3Api::class) // The list editor's bottom sheet.
private fun EntryProviderScope<NavKey>.pageEntries(navigator: Navigator) {
    entry<ListEditorNavKey>(metadata = BottomSheetSceneStrategy.bottomSheet()) { key ->
        ListEditorRoute(
            novelId = key.novelId,
            novelTitle = key.novelTitle,
            onClose = navigator::goBack,
        )
    }
    entry<ReleaseNavKey> { key ->
        ReleaseRoute(
            releaseId = key.id,
            onNavigateBack = { navigator.goBack() },
            onOpenNovel = { id -> navigator.navigate(NovelDetailNavKey(id)) },
            onOpenProducer = { id -> navigator.navigate(ProducerNavKey(id)) },
        )
    }
    entry<TagNavKey> { key ->
        BrowseRoute(
            scope = BrowseScope.Tag(key.id),
            onNavigateBack = { navigator.goBack() },
            onOpenNovel = { id -> navigator.navigate(NovelDetailNavKey(id)) },
        )
    }
    entry<ProducerNavKey> { key ->
        BrowseRoute(
            scope = BrowseScope.Producer(key.id),
            onNavigateBack = { navigator.goBack() },
            onOpenNovel = { id -> navigator.navigate(NovelDetailNavKey(id)) },
        )
    }
    entry<StaffNavKey> { key ->
        BrowseRoute(
            scope = BrowseScope.Staff(key.id),
            onNavigateBack = { navigator.goBack() },
            onOpenNovel = { id -> navigator.navigate(NovelDetailNavKey(id)) },
        )
    }
    entry<CharacterNavKey> { key ->
        CharacterRoute(
            characterId = key.id,
            onNavigateBack = { navigator.goBack() },
            onOpenNovel = { id -> navigator.navigate(NovelDetailNavKey(id)) },
            onOpenStaff = { id -> navigator.navigate(StaffNavKey(id)) },
        )
    }
}
