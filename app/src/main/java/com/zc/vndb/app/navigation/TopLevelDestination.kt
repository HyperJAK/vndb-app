package com.zc.vndb.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText

/** The tabs of the navigation bar (or rail): each one's root key, icon and label. */
internal enum class TopLevelDestination(val key: NavKey, val icon: ImageVector, val label: UiText) {
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
