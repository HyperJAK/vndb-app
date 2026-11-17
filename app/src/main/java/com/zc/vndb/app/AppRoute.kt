package com.zc.vndb.app

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zc.vndb.app.navigation.AppNavigation
import com.zc.vndb.common.ui.component.wallpaper.WallpaperView
import com.zc.vndb.common.ui.mapper.toSpec
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.LocalShowSensitiveContent
import org.koin.compose.viewmodel.koinViewModel

/** A background brighter than this is a light theme, which needs dark status and navigation bar icons. */
private const val LIGHT_BACKGROUND_LUMINANCE = 0.5f

/** App root: applies the chosen theme and provides app-wide settings to the UI, then the navigation. */
@Composable
fun AppRoute(modifier: Modifier = Modifier, viewModel: AppViewModel = koinViewModel()) {
    val showSensitiveContent by viewModel.showSensitiveContent.collectAsStateWithLifecycle()
    val theme by viewModel.theme.collectAsStateWithLifecycle()
    // Until the stored theme is read, only the window background (the launch color) shows.
    theme?.let { appTheme ->
        val spec = appTheme.toSpec()
        val isLightTheme = spec.colorScheme.background.luminance() > LIGHT_BACKGROUND_LUMINANCE
        SystemBarIcons(isLightTheme = isLightTheme)
        VndbTheme(spec) {
            CompositionLocalProvider(LocalShowSensitiveContent provides showSensitiveContent) {
                WallpaperView(modifier = modifier) {
                    AppNavigation()
                }
            }
        }
    }
}

/** Dark status and navigation bar icons on a light theme, light icons otherwise, whatever the system mode. */
@Composable
private fun SystemBarIcons(isLightTheme: Boolean) {
    val window = LocalActivity.current?.window ?: return
    val view = LocalView.current
    SideEffect {
        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = isLightTheme
            isAppearanceLightNavigationBars = isLightTheme
        }
    }
}
