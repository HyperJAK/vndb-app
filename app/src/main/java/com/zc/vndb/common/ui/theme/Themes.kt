package com.zc.vndb.common.ui.theme

import com.zc.vndb.common.ui.resource.DrawableResource
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.schemes.HouseInFataMorganaScheme
import com.zc.vndb.common.ui.theme.schemes.NocturneScheme
import com.zc.vndb.common.ui.theme.schemes.SayaNoUtaScheme

// Every theme: a name, a color scheme from `schemes/` and an optional wallpaper. To add one, see
// docs/plans/10-themes.md ("Adding a theme").

/** A wallpaper kept low so text and cards stay readable over it. */
private const val WALLPAPER_ALPHA = 0.2f

private fun VndbScheme.toSpec(name: UiText, wallpaper: WallpaperSpec? = null) = VndbThemeSpec(
    name = name,
    colorScheme = colorScheme,
    colors = colors,
    wallpaper = wallpaper,
)

/** Nocturne VN on a plain background. */
val OriginalThemeSpec = NocturneScheme.toSpec(
    name = UiText.StringResource(StringResource.theme_original),
)

/** Nocturne VN over the Angelic Serenade wallpaper. */
val AngelicSerenadeThemeSpec = NocturneScheme.toSpec(
    name = UiText.StringResource(StringResource.theme_angelic_serenade),
    wallpaper = WallpaperSpec(DrawableResource.wallpaper_angelic_serenade, WALLPAPER_ALPHA),
)

/** Wine and teal over the Saya no Uta wallpaper. */
val SayaNoUtaThemeSpec = SayaNoUtaScheme.toSpec(
    name = UiText.StringResource(StringResource.theme_saya_no_uta),
    wallpaper = WallpaperSpec(DrawableResource.wallpaper_saya_no_uta, WALLPAPER_ALPHA),
)

/** Warm near-black and gold over the House in Fata Morgana wallpaper. */
val HouseInFataMorganaThemeSpec = HouseInFataMorganaScheme.toSpec(
    name = UiText.StringResource(StringResource.theme_house_in_fata_morgana),
    wallpaper = WallpaperSpec(DrawableResource.wallpaper_house_in_fata_morgana, WALLPAPER_ALPHA),
)
