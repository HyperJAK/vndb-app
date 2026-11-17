package com.zc.vndb.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zc.vndb.app.navigation.AppNavigation
import com.zc.vndb.common.ui.component.wallpaper.WallpaperView
import com.zc.vndb.common.ui.mapper.toSpec
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.LocalShowSensitiveContent
import org.koin.compose.viewmodel.koinViewModel

/** App root: applies the chosen theme and provides app-wide settings to the UI, then the navigation. */
@Composable
fun AppRoute(modifier: Modifier = Modifier, viewModel: AppViewModel = koinViewModel()) {
    val showSensitiveContent by viewModel.showSensitiveContent.collectAsStateWithLifecycle()
    val theme by viewModel.theme.collectAsStateWithLifecycle()
    // Until the stored theme is read, only the window background (the launch color) shows.
    theme?.let { appTheme ->
        VndbTheme(appTheme.toSpec()) {
            CompositionLocalProvider(LocalShowSensitiveContent provides showSensitiveContent) {
                WallpaperView(modifier = modifier) {
                    AppNavigation()
                }
            }
        }
    }
}
