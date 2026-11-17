package com.zc.vndb.common.ui.mapper

import com.zc.vndb.common.domain.model.AppTheme
import com.zc.vndb.common.ui.theme.AngelicSerenadeThemeSpec
import com.zc.vndb.common.ui.theme.OriginalThemeSpec
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AppThemeUIModelMapperTest {

    @Test
    fun `Original has no wallpaper and Angelic Serenade has one`() {
        assertEquals(OriginalThemeSpec, AppTheme.Original.toSpec())
        assertNull(AppTheme.Original.toSpec().wallpaper)
        assertEquals(AngelicSerenadeThemeSpec, AppTheme.AngelicSerenade.toSpec())
        assertNotNull(AppTheme.AngelicSerenade.toSpec().wallpaper)
    }

    @Test
    fun `both themes share the Nocturne palette for now`() {
        assertEquals(OriginalThemeSpec.colors, AngelicSerenadeThemeSpec.colors)
    }

    @Test
    fun `every theme has a distinct name`() {
        assertEquals(AppTheme.entries.size, AppTheme.entries.map { it.toLabel() }.toSet().size)
    }
}
