package com.zc.vndb.common.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

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
