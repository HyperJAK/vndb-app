package com.zc.vndb.common.ui.mapper

import com.zc.vndb.common.domain.model.AppTheme
import com.zc.vndb.common.ui.theme.AngelicSerenadeThemeSpec
import com.zc.vndb.common.ui.theme.OriginalThemeSpec
import com.zc.vndb.common.ui.theme.SayaNoUtaThemeSpec
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AppThemeUIModelMapperTest {

    @Test
    fun `every theme maps to its own spec`() {
        assertEquals(OriginalThemeSpec, AppTheme.Original.toSpec())
        assertEquals(AngelicSerenadeThemeSpec, AppTheme.AngelicSerenade.toSpec())
        assertEquals(SayaNoUtaThemeSpec, AppTheme.SayaNoUta.toSpec())
    }

    @Test
    fun `every theme has a distinct name and a distinct look`() {
        val specs = AppTheme.entries.map { it.toSpec() }

        assertEquals(AppTheme.entries.size, specs.map { it.name }.toSet().size)
        val looks = specs.map { Pair(it.colorScheme, it.wallpaper) }
        assertEquals(AppTheme.entries.size, looks.toSet().size)
        assertEquals(specs.map { it.name }, AppTheme.entries.map { it.toLabel() })
    }

    @Test
    fun `Original has no wallpaper and the other themes have one`() {
        assertNull(AppTheme.Original.toSpec().wallpaper)
        AppTheme.entries.filter { it != AppTheme.Original }.forEach {
            assertNotNull(it.toSpec().wallpaper, "$it needs a wallpaper")
        }
    }

    @Test
    fun `Original and Angelic Serenade share the Nocturne palette`() {
        assertEquals(OriginalThemeSpec.colors, AngelicSerenadeThemeSpec.colors)
        assertEquals(OriginalThemeSpec.colorScheme, AngelicSerenadeThemeSpec.colorScheme)
    }

    @Test
    fun `Saya no Uta has its own palette`() {
        assertNotEquals(OriginalThemeSpec.colorScheme, SayaNoUtaThemeSpec.colorScheme)
        assertNotEquals(OriginalThemeSpec.colors, SayaNoUtaThemeSpec.colors)
    }
}
