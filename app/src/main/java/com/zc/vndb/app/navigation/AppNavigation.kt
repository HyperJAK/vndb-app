package com.zc.vndb.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
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
import kotlinx.serialization.Serializable

@Serializable
data object DiscoverNavKey : NavKey

@Serializable
data object BrowseNavKey : NavKey

@Serializable
data object ProfileNavKey : NavKey

/** A destination shown full screen: the navigation bar (or rail) is hidden while it is on top. */
interface FullScreenNavKey : NavKey

/** Preferences and app info (pushed on the Profile tab). */
@Serializable
data object SettingsNavKey : FullScreenNavKey

/** Choose the app's colors and wallpaper (pushed from Settings). */
@Serializable
data object ThemeNavKey : FullScreenNavKey

/** Log in with a VNDB API token (pushed on the Profile tab). */
@Serializable
data object LoginNavKey : NavKey

/** A release's page. */
@Serializable
data class ReleaseNavKey(val id: String) : FullScreenNavKey

/** Browse scoped to one VNDB tag. */
@Serializable
data class TagNavKey(val id: String) : FullScreenNavKey

/** Browse scoped to one VNDB producer (developer). */
@Serializable
data class ProducerNavKey(val id: String) : FullScreenNavKey

/** A character's page. */
@Serializable
data class CharacterNavKey(val id: String) : FullScreenNavKey

/**
 * A destination shown over the page below it (a bottom sheet). The navigation bar keeps the
 * state of that page: shown over a tab, hidden over a full-screen page.
 */
interface OverlayNavKey : NavKey

/** The signed-in user's list entry for a VN, edited in a bottom sheet. */
@Serializable
data class ListEditorNavKey(val novelId: String, val novelTitle: String) : OverlayNavKey

/** A visual novel's detail page. */
@Serializable
data class NovelDetailNavKey(val id: String) : FullScreenNavKey

private enum class TopLevelDestination(val key: NavKey, val icon: ImageVector, val label: UiText) {
    Discover(
        DiscoverNavKey,
        Icons.Outlined.Explore,
        UiText.StringResource(StringResource.nav_discover),
    ),
    Browse(BrowseNavKey, Icons.Outlined.Tune, UiText.StringResource(StringResource.nav_browse)),
    Profile(
        ProfileNavKey,
        Icons.Outlined.Person,
        UiText.StringResource(StringResource.nav_profile),
    ),
}

/** App root: adaptive navigation suite (bar / rail / drawer) around the Navigation 3 back stack. */
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navigationState = rememberNavigationState(
        startRoute = DiscoverNavKey,
        topLevelRoutes = remember { TopLevelDestination.entries.map { it.key }.toSet() },
    )
    val navigator = remember(navigationState) { Navigator(navigationState) }
    val itemColors = navigationItemColors()

    // Full-screen pages slide the navigation bar (or rail) away while they are on top.
    val currentKey = navigationState.backStacks[navigationState.topLevelRoute]
        ?.lastOrNull { it !is OverlayNavKey }
    val isNavigationHidden = currentKey is FullScreenNavKey
    val scaffoldState = rememberNavigationSuiteScaffoldState()
    LaunchedEffect(isNavigationHidden) {
        if (isNavigationHidden) scaffoldState.hide() else scaffoldState.show()
    }

    NavigationSuiteScaffold(
        modifier = modifier,
        state = scaffoldState,
        containerColor = Color.Transparent,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = VndbTheme.colors.surfaceInset,
            navigationRailContainerColor = VndbTheme.colors.surfaceInset,
            navigationDrawerContainerColor = VndbTheme.colors.surfaceInset,
        ),
        navigationSuiteItems = {
            TopLevelDestination.entries.forEach { destination ->
                val selected = navigationState.topLevelRoute == destination.key
                item(
                    selected = selected,
                    onClick = {
                        if (selected) {
                            navigator.reselect(
                                destination.key,
                            )
                        } else {
                            navigator.navigate(destination.key)
                        }
                    },
                    icon = { Icon(destination.icon, contentDescription = null) },
                    label = { Text(destination.label.asString()) },
                    colors = itemColors,
                )
            }
        },
    ) {
        AppNavDisplay(navigationState, navigator)
    }
}

/** Selected tab in the accent color on an elevated-surface indicator pill, as in the design. */
@Composable
private fun navigationItemColors(): NavigationSuiteItemColors {
    val accent = VndbTheme.colors.accent
    val idle = MaterialTheme.colorScheme.onSurfaceVariant
    val indicator = VndbTheme.colors.surfaceElevated
    return NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = accent,
            selectedTextColor = accent,
            indicatorColor = indicator,
            unselectedIconColor = idle,
            unselectedTextColor = idle,
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            selectedIconColor = accent,
            selectedTextColor = accent,
            indicatorColor = indicator,
            unselectedIconColor = idle,
            unselectedTextColor = idle,
        ),
    )
}

@Composable
private fun AppNavDisplay(navigationState: NavigationState, navigator: Navigator) {
    val bottomSheetStrategy = remember { BottomSheetSceneStrategy<NavKey>() }
    NavDisplay(
        entries = navigationState.toDecoratedEntries(appEntryProvider(navigator)),
        onBack = navigator::goBack,
        sceneStrategies = listOf(bottomSheetStrategy),
    )
}

private fun appEntryProvider(navigator: Navigator) = entryProvider<NavKey> {
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
    entry<CharacterNavKey> { key ->
        CharacterRoute(
            characterId = key.id,
            onNavigateBack = { navigator.goBack() },
            onOpenNovel = { id -> navigator.navigate(NovelDetailNavKey(id)) },
        )
    }
}
