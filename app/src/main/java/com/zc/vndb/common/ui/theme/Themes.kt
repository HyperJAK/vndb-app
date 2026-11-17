package com.zc.vndb.common.ui.theme

import androidx.annotation.DrawableRes
import com.zc.vndb.common.ui.resource.DrawableResource
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.schemes.HouseInFataMorganaScheme
import com.zc.vndb.common.ui.theme.schemes.NocturneScheme
import com.zc.vndb.common.ui.theme.schemes.SayaNoUtaScheme
import com.zc.vndb.common.ui.theme.schemes.TsukihimeScheme

// Every theme: a name, a color scheme from `schemes/` and an optional wallpaper. To add one, see
// docs/plans/10-themes.md ("Adding a theme").

/** A wallpaper kept low so text and cards stay readable over it. */
private const val WALLPAPER_ALPHA = 0.2f

/** How opaque cards are over a wallpaper: low enough for it to show through, high enough to read. */
private const val CARD_ALPHA = 0.7f

private fun wallpaper(@DrawableRes image: Int) = WallpaperSpec(image, WALLPAPER_ALPHA, CARD_ALPHA)

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
    wallpaper = wallpaper(DrawableResource.wallpaper_angelic_serenade),
)

/** Wine and teal over the Saya no Uta wallpaper. */
val SayaNoUtaThemeSpec = SayaNoUtaScheme.toSpec(
    name = UiText.StringResource(StringResource.theme_saya_no_uta),
    wallpaper = wallpaper(DrawableResource.wallpaper_saya_no_uta),
)

/** Warm near-black and gold over the House in Fata Morgana wallpaper. */
val HouseInFataMorganaThemeSpec = HouseInFataMorganaScheme.toSpec(
    name = UiText.StringResource(StringResource.theme_house_in_fata_morgana),
    wallpaper = wallpaper(DrawableResource.wallpaper_house_in_fata_morgana),
)

/** Midnight blue and crimson over the Tsukihime wallpaper. */
val TsukihimeThemeSpec = TsukihimeScheme.toSpec(
    name = UiText.StringResource(StringResource.theme_tsukihime),
    wallpaper = wallpaper(DrawableResource.wallpaper_tsukihime),
)
