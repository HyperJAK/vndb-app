package com.zc.vndb.common.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Raw palette from DESIGN.md (Nocturne VN). Screens never read these directly:
// use MaterialTheme.colorScheme or VndbTheme.colors.

// Material 3 roles
internal val Surface = Color(0xFF0D131F)
internal val SurfaceDim = Color(0xFF0D131F)
internal val SurfaceBright = Color(0xFF333947)
internal val SurfaceContainerLowest = Color(0xFF080E1A)
internal val SurfaceContainerLow = Color(0xFF161C28)
internal val SurfaceContainer = Color(0xFF1A202C)
internal val SurfaceContainerHigh = Color(0xFF242A37)
internal val SurfaceContainerHighest = Color(0xFF2F3542)
internal val OnSurface = Color(0xFFDDE2F4)
internal val OnSurfaceVariant = Color(0xFFBDC8D1)
internal val InverseSurface = Color(0xFFDDE2F4)
internal val InverseOnSurface = Color(0xFF2B303E)
internal val Outline = Color(0xFF87929A)
internal val OutlineVariant = Color(0xFF3E484F)
internal val SurfaceTint = Color(0xFF7BD0FF)
internal val Primary = Color(0xFF8ED5FF)
internal val OnPrimary = Color(0xFF00354A)
internal val PrimaryContainer = Color(0xFF38BDF8)
internal val OnPrimaryContainer = Color(0xFF004965)
internal val InversePrimary = Color(0xFF00668A)
internal val Secondary = Color(0xFFFFB95F)
internal val OnSecondary = Color(0xFF472A00)
internal val SecondaryContainer = Color(0xFFEE9800)
internal val OnSecondaryContainer = Color(0xFF5B3800)
internal val Tertiary = Color(0xFFAECFFF)
internal val OnTertiary = Color(0xFF00315C)
internal val TertiaryContainer = Color(0xFF8AB4EC)
internal val OnTertiaryContainer = Color(0xFF124577)
internal val Error = Color(0xFFFFB4AB)
internal val OnError = Color(0xFF690005)
internal val ErrorContainer = Color(0xFF93000A)
internal val OnErrorContainer = Color(0xFFFFDAD6)
internal val Background = Color(0xFF0D131F)
internal val OnBackground = Color(0xFFDDE2F4)
internal val SurfaceVariant = Color(0xFF2F3542)
internal val Scrim = Color(0xFF000000)

// Brand & semantic colors
internal val SkyBlue = Color(0xFF38BDF8)
internal val SkyBluePressed = Color(0xFF0284C7)
internal val Amber = Color(0xFFF59E0B)
internal val VndbDeepBlue = Color(0xFF18497B)
internal val SurfaceCard = Color(0xFF1A233A)
internal val SurfaceElevated = Color(0xFF232F4D)
internal val SurfaceInset = Color(0xFF141C2E)
internal val BorderSubtle = Color(0xFF253352)
internal val Emerald = Color(0xFF10B981)
internal val Red = Color(0xFFEF4444)
internal val Rose = Color(0xFFF43F5E)
internal val Slate = Color(0xFF64748B)
internal val Violet = Color(0xFFA78BFA)

/** A pill/badge treatment: tinted fill, text and hairline border. */
@Immutable
data class TintedColors(val container: Color, val content: Color, val border: Color)

private fun tinted(base: Color, containerAlpha: Float = 0.12f, borderAlpha: Float = 0.25f) =
    TintedColors(
        container = base.copy(alpha = containerAlpha),
        content = base,
        border = base.copy(alpha = borderAlpha),
    )

/** Colors from DESIGN.md that have no Material 3 role. Read through [VndbTheme.colors]. */
@Immutable
data class VndbColors(
    // Surfaces (elevation levels 1–3)
    val surfaceCard: Color = SurfaceCard,
    val surfaceElevated: Color = SurfaceElevated,
    val surfaceInset: Color = SurfaceInset,
    val surfaceGlass: Color = SurfaceInset.copy(alpha = 0.85f),
    val glassHairline: Color = Color.White.copy(alpha = 0.08f),
    val borderSubtle: Color = BorderSubtle,
    val borderFocused: Color = SkyBlue.copy(alpha = 0.35f),
    val focusRing: Color = SkyBlue.copy(alpha = 0.2f),
    val glow: Color = SkyBluePressed.copy(alpha = 0.15f),
    val coverScrim: Color = Surface.copy(alpha = 0.85f),

    // Brand accents
    val accent: Color = SkyBlue,
    val accentPressed: Color = SkyBluePressed,
    val onAccent: Color = Surface,
    val score: Color = Amber,
    val brandDeepBlue: Color = VndbDeepBlue,
    val placeholder: Color = Slate,

    // Spoiler tiers & content flags
    val spoilerNone: TintedColors = tinted(Emerald),
    val spoilerMinor: TintedColors = tinted(Amber),
    val spoilerMajor: TintedColors = tinted(Red, containerAlpha = 0.15f, borderAlpha = 0.35f),
    val tagNsfw: TintedColors = tinted(Rose),

    // Neutral metadata chips (tags, platforms)
    val chipNeutral: TintedColors = TintedColors(
        container = SurfaceElevated,
        content = OnSurfaceVariant,
        border = BorderSubtle,
    ),

    // Length category pill on Title Detail
    val chipLength: TintedColors = tinted(SkyBlue),

    // Release labels (Discover "New & Trending")
    val releaseNew: TintedColors = tinted(Tertiary),
    val releaseTranslation: TintedColors = tinted(SkyBlue),
    val releasePatch: TintedColors = tinted(Amber),
    val releaseRerelease: TintedColors = tinted(Emerald),

    // Tracking status badges
    val statusPlaying: TintedColors = tinted(SkyBlue),
    val statusFinished: TintedColors = tinted(Emerald),
    val statusStalled: TintedColors = tinted(Amber),
    val statusDropped: TintedColors = tinted(Red),

    // List labels: outline only, so they stay quieter than the status
    val labelWishlist: TintedColors = TintedColors(Color.Transparent, Violet, Violet),
    val labelBlacklist: TintedColors = TintedColors(Color.Transparent, OnSurfaceVariant, Slate),
    val labelOwn: TintedColors = TintedColors(Color.Transparent, OnSurfaceVariant, BorderSubtle),

    // Filter chips
    val chipInactiveContainer: Color = SurfaceCard,
    val chipActiveContainer: Color = VndbDeepBlue,
    val chipActiveContent: Color = SkyBlue,
)

internal val LocalVndbColors = staticCompositionLocalOf { VndbColors() }
