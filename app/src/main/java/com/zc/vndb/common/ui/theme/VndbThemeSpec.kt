package com.zc.vndb.common.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

/** An image drawn full screen behind the app, cropped to fit, at [alpha] over the background. */
@Immutable
data class WallpaperSpec(@DrawableRes val image: Int, val alpha: Float)

/** Everything the UI needs to paint one theme. Read through [VndbTheme]. */
@Immutable
data class VndbThemeSpec(
    val colorScheme: ColorScheme,
    val colors: VndbColors,
    /** Null means a plain background. */
    val wallpaper: WallpaperSpec?,
)

internal val LocalVndbWallpaper = staticCompositionLocalOf<WallpaperSpec?> { null }
