package com.zc.vndb.common.ui.theme

import androidx.compose.material3.darkColorScheme
import com.zc.vndb.common.ui.resource.DrawableResource

// Nocturne VN is a dark-only, OLED-tuned palette, so there is no light scheme and no dynamic color.
internal val NocturneColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    inversePrimary = InversePrimary,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    surfaceTint = SurfaceTint,
    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface,
    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    outline = Outline,
    outlineVariant = OutlineVariant,
    scrim = Scrim,
    surfaceBright = SurfaceBright,
    surfaceDim = SurfaceDim,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,
)

/** Nocturne VN on a plain background. */
val OriginalThemeSpec = VndbThemeSpec(
    colorScheme = NocturneColorScheme,
    colors = VndbColors(),
    wallpaper = null,
)

private const val ANGELIC_SERENADE_WALLPAPER_ALPHA = 0.2f

/** Nocturne VN over the Angelic Serenade wallpaper, kept low so text and cards stay readable. */
val AngelicSerenadeThemeSpec = OriginalThemeSpec.copy(
    wallpaper = WallpaperSpec(
        image = DrawableResource.wallpaper_angelic_serenade,
        alpha = ANGELIC_SERENADE_WALLPAPER_ALPHA,
    ),
)
