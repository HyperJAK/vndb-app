package com.zc.vndb.common.ui.mapper

import com.zc.vndb.common.domain.model.AppTheme
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.AngelicSerenadeThemeSpec
import com.zc.vndb.common.ui.theme.HouseInFataMorganaThemeSpec
import com.zc.vndb.common.ui.theme.OriginalThemeSpec
import com.zc.vndb.common.ui.theme.SayaNoUtaThemeSpec
import com.zc.vndb.common.ui.theme.VndbThemeSpec

/** The colors, wallpaper and name of this theme. Exhaustive: a new [AppTheme] will not compile without a spec. */
fun AppTheme.toSpec(): VndbThemeSpec = when (this) {
    AppTheme.Original -> OriginalThemeSpec
    AppTheme.AngelicSerenade -> AngelicSerenadeThemeSpec
    AppTheme.SayaNoUta -> SayaNoUtaThemeSpec
    AppTheme.HouseInFataMorgana -> HouseInFataMorganaThemeSpec
}

/** "Original", "Angelic Serenade", "Saya no Uta", "House in Fata Morgana". */
fun AppTheme.toLabel(): UiText = toSpec().name
