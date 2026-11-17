package com.zc.vndb.common.ui.mapper

import com.zc.vndb.common.domain.model.AppTheme
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.AngelicSerenadeThemeSpec
import com.zc.vndb.common.ui.theme.DarkThemeSpec
import com.zc.vndb.common.ui.theme.HouseInFataMorganaThemeSpec
import com.zc.vndb.common.ui.theme.LightThemeSpec
import com.zc.vndb.common.ui.theme.MeikeiNoLupercaliaThemeSpec
import com.zc.vndb.common.ui.theme.NeonThemeSpec
import com.zc.vndb.common.ui.theme.OriginalThemeSpec
import com.zc.vndb.common.ui.theme.SayaNoUtaThemeSpec
import com.zc.vndb.common.ui.theme.TsukihimeThemeSpec
import com.zc.vndb.common.ui.theme.VndbThemeSpec

/** The colors, wallpaper and name of this theme. Exhaustive: a new [AppTheme] will not compile without a spec. */
fun AppTheme.toSpec(): VndbThemeSpec = when (this) {
    AppTheme.Original -> OriginalThemeSpec
    AppTheme.Light -> LightThemeSpec
    AppTheme.Dark -> DarkThemeSpec
    AppTheme.Neon -> NeonThemeSpec
    AppTheme.AngelicSerenade -> AngelicSerenadeThemeSpec
    AppTheme.SayaNoUta -> SayaNoUtaThemeSpec
    AppTheme.HouseInFataMorgana -> HouseInFataMorganaThemeSpec
    AppTheme.Tsukihime -> TsukihimeThemeSpec
    AppTheme.MeikeiNoLupercalia -> MeikeiNoLupercaliaThemeSpec
}

/** The theme's display name, e.g. "Original" or "Meikei no Lupercalia". */
fun AppTheme.toLabel(): UiText = toSpec().name
