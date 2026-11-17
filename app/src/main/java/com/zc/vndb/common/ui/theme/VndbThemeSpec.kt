package com.zc.vndb.common.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import com.zc.vndb.common.ui.text.UiText

/**
 * An image drawn full screen behind the app, cropped to fit, at [alpha] over the background.
 * Cards are drawn at [cardAlpha] so the wallpaper shows slightly through them.
 */
@Immutable
data class WallpaperSpec(@DrawableRes val image: Int, val alpha: Float, val cardAlpha: Float)

/**
 * A color palette: the Material roles and the extra design tokens. Each one is defined in its own
 * file in `schemes/`, and several themes may share one.
 */
@Immutable
data class VndbScheme(val colorScheme: ColorScheme, val colors: VndbColors)

/** Everything the UI needs to paint one theme. Read through [VndbTheme]. */
@Immutable
data class VndbThemeSpec(
    val name: UiText,
    val colorScheme: ColorScheme,
    val colors: VndbColors,
    /** Null means a plain background. */
    val wallpaper: WallpaperSpec?,
)

internal val LocalVndbWallpaper = staticCompositionLocalOf<WallpaperSpec?> { null }
