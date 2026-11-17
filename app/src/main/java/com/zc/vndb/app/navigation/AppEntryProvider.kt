package com.zc.vndb.app.navigation

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import com.zc.vndb.common.ui.navigation.Navigator
import com.zc.vndb.feature.browse.presentation.navigation.BrowseNavKey
import com.zc.vndb.feature.browse.presentation.navigation.ProducerNavKey
import com.zc.vndb.feature.browse.presentation.navigation.StaffNavKey
import com.zc.vndb.feature.browse.presentation.navigation.TagNavKey
import com.zc.vndb.feature.browse.presentation.navigation.browseEntry
import com.zc.vndb.feature.browse.presentation.navigation.browseScopedEntries
import com.zc.vndb.feature.character.presentation.navigation.CharacterNavKey
import com.zc.vndb.feature.character.presentation.navigation.characterEntry
import com.zc.vndb.feature.detail.presentation.navigation.DetailNavActions
import com.zc.vndb.feature.detail.presentation.navigation.DetailNavKey
import com.zc.vndb.feature.detail.presentation.navigation.detailEntry
import com.zc.vndb.feature.discover.presentation.navigation.discoverEntry
import com.zc.vndb.feature.listeditor.presentation.navigation.ListEditorNavKey
import com.zc.vndb.feature.listeditor.presentation.navigation.listEditorEntry
import com.zc.vndb.feature.login.presentation.navigation.LoginNavKey
import com.zc.vndb.feature.login.presentation.navigation.loginEntry
import com.zc.vndb.feature.profile.presentation.navigation.profileEntry
import com.zc.vndb.feature.release.presentation.navigation.ReleaseNavKey
import com.zc.vndb.feature.release.presentation.navigation.releaseEntry
import com.zc.vndb.feature.settings.presentation.navigation.SettingsNavKey
import com.zc.vndb.feature.settings.presentation.navigation.settingsEntry
import com.zc.vndb.feature.theme.presentation.navigation.ThemeNavKey
import com.zc.vndb.feature.theme.presentation.navigation.themeEntry

/**
 * Registers every feature's entries. The only place that knows all the features: each one gets
 * the lambdas that open the others' screens, so features never depend on each other.
 */
internal fun appEntryProvider(navigator: Navigator): (NavKey) -> NavEntry<NavKey> {
    val openNovel: (String) -> Unit = { navigator.navigate(DetailNavKey(it)) }
    val openStaff: (String) -> Unit = { navigator.navigate(StaffNavKey(it)) }
    val openProducer: (String) -> Unit = { navigator.navigate(ProducerNavKey(it)) }
    val openListEditor: (String, String) -> Unit = { id, title ->
        navigator.navigate(ListEditorNavKey(id, title))
    }
    return entryProvider {
        discoverEntry(
            onOpenNovel = openNovel,
            onOpenBrowse = { navigator.navigate(BrowseNavKey) },
        )
        browseEntry(onOpenNovel = openNovel)
        browseScopedEntries(onNavigateBack = navigator::goBack, onOpenNovel = openNovel)
        profileEntry(
            onOpenLogin = { navigator.navigate(LoginNavKey) },
            onOpenSettings = { navigator.navigate(SettingsNavKey) },
            onOpenNovel = openNovel,
            onOpenListEditor = openListEditor,
        )
        settingsEntry(
            onNavigateBack = navigator::goBack,
            onOpenTheme = { navigator.navigate(ThemeNavKey) },
        )
        themeEntry(onNavigateBack = navigator::goBack)
        loginEntry(onNavigateBack = navigator::goBack)
        detailEntry(
            DetailNavActions(
                onNavigateBack = navigator::goBack,
                onOpenNovel = openNovel,
                onOpenCharacter = { navigator.navigate(CharacterNavKey(it)) },
                onOpenTag = { navigator.navigate(TagNavKey(it)) },
                onOpenProducer = openProducer,
                onOpenStaff = openStaff,
                onOpenRelease = { navigator.navigate(ReleaseNavKey(it)) },
                onOpenListEditor = openListEditor,
            ),
        )
        releaseEntry(
            onNavigateBack = navigator::goBack,
            onOpenNovel = openNovel,
            onOpenProducer = openProducer,
        )
        characterEntry(
            onNavigateBack = navigator::goBack,
            onOpenNovel = openNovel,
            onOpenStaff = openStaff,
        )
        listEditorEntry(onClose = navigator::goBack)
    }
}
