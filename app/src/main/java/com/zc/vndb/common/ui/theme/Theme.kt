package com.zc.vndb.common.ui.theme

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/** Applies [spec]: Material colors, the extra design tokens and the wallpaper. */
@Composable
fun VndbTheme(spec: VndbThemeSpec = OriginalThemeSpec, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalVndbColors provides spec.colors,
        LocalVndbWallpaper provides spec.wallpaper,
        LocalVndbShapes provides VndbExtraShapes(),
        LocalVndbSpacing provides VndbSpacing(),
        LocalVndbDimens provides VndbDimens(),
    ) {
        MaterialTheme(
            colorScheme = spec.colorScheme,
            typography = VndbTypography,
            shapes = VndbShapes,
            content = content,
        )
    }
}

/**
 * A card's background: [color] at the theme's card opacity, so the wallpaper shows slightly through
 * it. Solid when the theme has no wallpaper. For on-page cards only: dialogs, sheets and menus stay
 * solid, so they use the plain color tokens.
 */
@Composable
fun Modifier.cardBackground(color: Color = VndbTheme.colors.surfaceCard): Modifier =
    background(color.copy(alpha = VndbTheme.wallpaper?.cardAlpha ?: 1f))

/** Design tokens that extend [MaterialTheme]. */
object VndbTheme {
    val colors: VndbColors
        @Composable @ReadOnlyComposable
        get() = LocalVndbColors.current

    /** The wallpaper behind the app; null for a plain background. */
    val wallpaper: WallpaperSpec?
        @Composable @ReadOnlyComposable
        get() = LocalVndbWallpaper.current

    val shapes: VndbExtraShapes
        @Composable @ReadOnlyComposable
        get() = LocalVndbShapes.current

    val spacing: VndbSpacing
        @Composable @ReadOnlyComposable
        get() = LocalVndbSpacing.current

    val dimens: VndbDimens
        @Composable @ReadOnlyComposable
        get() = LocalVndbDimens.current
}
