package com.zc.vndb.app.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.zc.vndb.R
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.feature.browse.presentation.BrowseRoute
import com.zc.vndb.feature.detail.presentation.DetailRoute
import com.zc.vndb.feature.discover.presentation.DiscoverRoute
import kotlinx.serialization.Serializable

@Serializable
data object DiscoverNavKey : NavKey

@Serializable
data object BrowseNavKey : NavKey

@Serializable
data object ProfileNavKey : NavKey

/** A visual novel's detail page. */
@Serializable
data class NovelDetailNavKey(val id: String) : NavKey

private enum class TopLevelDestination(
    val key: NavKey,
    val icon: ImageVector,
    val label: StringResource,
) {
    Discover(DiscoverNavKey, Icons.Outlined.Explore, R.string.nav_discover),
    Browse(BrowseNavKey, Icons.Outlined.Tune, R.string.nav_browse),
    Profile(ProfileNavKey, Icons.Outlined.Person, R.string.nav_profile),
}

/** App root: adaptive navigation suite (bar / rail / drawer) around the Navigation 3 back stack. */
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(DiscoverNavKey)
    val root = backStack.firstOrNull()
    val itemColors = navigationItemColors()

    NavigationSuiteScaffold(
        modifier = modifier,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = VndbTheme.colors.surfaceInset,
            navigationRailContainerColor = VndbTheme.colors.surfaceInset,
            navigationDrawerContainerColor = VndbTheme.colors.surfaceInset,
        ),
        navigationSuiteItems = {
            TopLevelDestination.entries.forEach { destination ->
                item(
                    selected = root == destination.key,
                    onClick = {
                        if (backStack.size != 1 || root != destination.key) {
                            backStack.clear()
                            backStack.add(destination.key)
                        }
                    },
                    icon = { Icon(destination.icon, contentDescription = null) },
                    label = { Text(stringResource(destination.label)) },
                    colors = itemColors,
                )
            }
        },
    ) {
        AppNavDisplay(backStack)
    }
}

/** Selected tab in the accent color with no indicator pill, as in the design. */
@Composable
private fun navigationItemColors(): NavigationSuiteItemColors {
    val accent = VndbTheme.colors.accent
    val idle = MaterialTheme.colorScheme.onSurfaceVariant
    return NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = accent,
            selectedTextColor = accent,
            indicatorColor = Color.Transparent,
            unselectedIconColor = idle,
            unselectedTextColor = idle,
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            selectedIconColor = accent,
            selectedTextColor = accent,
            indicatorColor = Color.Transparent,
            unselectedIconColor = idle,
            unselectedTextColor = idle,
        ),
    )
}

@Composable
private fun AppNavDisplay(backStack: NavBackStack<NavKey>) {
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<DiscoverNavKey> {
                DiscoverRoute(
                    onOpenNovel = { id -> backStack.add(NovelDetailNavKey(id)) },
                    onOpenBrowse = {
                        backStack.clear()
                        backStack.add(BrowseNavKey)
                    },
                )
            }
            entry<BrowseNavKey> {
                BrowseRoute(onOpenNovel = { id -> backStack.add(NovelDetailNavKey(id)) })
            }
            entry<ProfileNavKey> {
                PlaceholderScreen(
                    text = stringResource(R.string.placeholder_coming_soon),
                )
            }
            entry<NovelDetailNavKey> { key ->
                DetailRoute(
                    novelId = key.id,
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onOpenNovel = { id -> backStack.add(NovelDetailNavKey(id)) },
                )
            }
        },
    )
}

/** Empty screen for destinations whose features have not landed yet. */
@Composable
private fun PlaceholderScreen(text: String, onBack: (() -> Unit)? = null) {
    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(VndbTheme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(
                VndbTheme.spacing.lg,
                Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            onBack?.let { back ->
                IconButton(onClick = back) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.cd_navigate_back),
                    )
                }
            }
        }
    }
}
