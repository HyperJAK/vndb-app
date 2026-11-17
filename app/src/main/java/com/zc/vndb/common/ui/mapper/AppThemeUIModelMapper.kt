package com.zc.vndb.common.ui.mapper

import com.zc.vndb.common.domain.model.AppTheme
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.AngelicSerenadeThemeSpec
import com.zc.vndb.common.ui.theme.OriginalThemeSpec
import com.zc.vndb.common.ui.theme.VndbThemeSpec

/** The colors and wallpaper that paint this theme. */
fun AppTheme.toSpec(): VndbThemeSpec = when (this) {
    AppTheme.Original -> OriginalThemeSpec
    AppTheme.AngelicSerenade -> AngelicSerenadeThemeSpec
}

/** "Original", "Angelic Serenade". */
fun AppTheme.toLabel(): UiText = UiText.StringResource(
    when (this) {
        AppTheme.Original -> StringResource.theme_original
        AppTheme.AngelicSerenade -> StringResource.theme_angelic_serenade
    },
)
