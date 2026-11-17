package com.zc.vndb.app.navigation

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import com.zc.vndb.common.ui.navigation.Navigator
import com.zc.vndb.feature.browse.presentation.navigation.browseEntry
import com.zc.vndb.feature.browse.presentation.navigation.browseScopedEntries
import com.zc.vndb.feature.browse.presentation.navigation.navigateToBrowse
import com.zc.vndb.feature.browse.presentation.navigation.navigateToProducer
import com.zc.vndb.feature.browse.presentation.navigation.navigateToStaff
import com.zc.vndb.feature.browse.presentation.navigation.navigateToTag
import com.zc.vndb.feature.character.presentation.navigation.characterEntry
import com.zc.vndb.feature.character.presentation.navigation.navigateToCharacter
import com.zc.vndb.feature.detail.presentation.navigation.DetailNavActions
import com.zc.vndb.feature.detail.presentation.navigation.detailEntry
import com.zc.vndb.feature.detail.presentation.navigation.navigateToDetail
import com.zc.vndb.feature.discover.presentation.navigation.discoverEntry
import com.zc.vndb.feature.listeditor.presentation.navigation.listEditorEntry
import com.zc.vndb.feature.listeditor.presentation.navigation.navigateToListEditor
import com.zc.vndb.feature.login.presentation.navigation.loginEntry
import com.zc.vndb.feature.login.presentation.navigation.navigateToLogin
import com.zc.vndb.feature.profile.presentation.navigation.profileEntry
import com.zc.vndb.feature.release.presentation.navigation.navigateToRelease
import com.zc.vndb.feature.release.presentation.navigation.releaseEntry
import com.zc.vndb.feature.settings.presentation.navigation.navigateToSettings
import com.zc.vndb.feature.settings.presentation.navigation.settingsEntry
import com.zc.vndb.feature.theme.presentation.navigation.navigateToTheme
import com.zc.vndb.feature.theme.presentation.navigation.themeEntry

/**
 * Registers every feature's entries. The only place that knows all the features: each one gets
 * the lambdas that open the others' screens, so features never depend on each other.
 */
internal fun appEntryProvider(navigator: Navigator): (NavKey) -> NavEntry<NavKey> {
    val openNovel: (String) -> Unit = { navigator.navigateToDetail(it) }
    val openStaff: (String) -> Unit = { navigator.navigateToStaff(it) }
    val openProducer: (String) -> Unit = { navigator.navigateToProducer(it) }
    val openListEditor: (String, String) -> Unit = { id, title ->
        navigator.navigateToListEditor(id, title)
    }
    return entryProvider {
        discoverEntry(
            onOpenNovel = openNovel,
            onOpenBrowse = { navigator.navigateToBrowse() },
        )
        browseEntry(onOpenNovel = openNovel)
        browseScopedEntries(onNavigateBack = navigator::goBack, onOpenNovel = openNovel)
        profileEntry(
            onOpenLogin = { navigator.navigateToLogin() },
            onOpenSettings = { navigator.navigateToSettings() },
            onOpenNovel = openNovel,
            onOpenListEditor = openListEditor,
        )
        settingsEntry(
            onNavigateBack = navigator::goBack,
            onOpenTheme = { navigator.navigateToTheme() },
        )
        themeEntry(onNavigateBack = navigator::goBack)
        loginEntry(onNavigateBack = navigator::goBack)
        detailEntry(
            DetailNavActions(
                onNavigateBack = navigator::goBack,
                onOpenNovel = openNovel,
                onOpenCharacter = { navigator.navigateToCharacter(it) },
                onOpenTag = { navigator.navigateToTag(it) },
                onOpenProducer = openProducer,
                onOpenStaff = openStaff,
                onOpenRelease = { navigator.navigateToRelease(it) },
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
