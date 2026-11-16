package com.zc.vndb.feature.release.presentation.component.releasehero.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.release.domain.model.ReleaseImage
import com.zc.vndb.feature.release.domain.model.ReleaseImageType
import com.zc.vndb.feature.release.domain.model.ReleaseMedium
import com.zc.vndb.feature.release.domain.model.ReleaseSamples.cover
import com.zc.vndb.feature.release.domain.model.ReleaseSamples.release
import com.zc.vndb.feature.release.presentation.component.releasehero.model.ReleaseFlagTone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ReleaseHeroUIModelMapperTest {

    @Test
    fun `facts list what VNDB knows, formatted`() {
        val facts = release().toHeroUIModel().facts

        assertEquals(
            listOf(
                R.string.release_released,
                R.string.release_platforms,
                R.string.release_media,
                R.string.release_engine,
                R.string.release_resolution,
                R.string.release_voiced,
                R.string.release_catalog,
                R.string.release_barcode,
            ),
            facts.map { it.label },
        )
        assertEquals(UiText.StringValue("2010-08-26"), facts[0].value)
        assertEquals(UiText.StringValue("Windows"), facts[1].value)
        assertEquals(UiText.StringResource(R.string.release_medium_dvd), facts[2].value)
        assertEquals(
            UiText.StringResource(R.string.release_resolution_value, listOf(1024, 576)),
            facts[4].value,
        )
        assertEquals(UiText.StringResource(R.string.release_voiced_full), facts[5].value)
    }

    @Test
    fun `several discs show a quantity`() {
        val media = release(media = listOf(ReleaseMedium("blr", 2))).toHeroUIModel().facts[2].value

        assertEquals(
            UiText.StringResource(
                R.string.release_medium_quantity,
                listOf(UiText.StringResource(R.string.release_medium_blr), 2),
            ),
            media,
        )
    }

    @Test
    fun `flags reflect the release`() {
        val flags = release().copy(
            isOfficial = false,
            isPatch = true,
            isFreeware = true,
            isUncensored = true,
            hasAdultContent = true,
            minAge = 18,
        )
            .toHeroUIModel().flags

        assertEquals(
            listOf(
                ReleaseFlagTone.Caution,
                ReleaseFlagTone.Caution,
                ReleaseFlagTone.Safe,
                ReleaseFlagTone.Neutral,
                ReleaseFlagTone.Adult,
                ReleaseFlagTone.Neutral,
            ),
            flags.map { it.tone },
        )
        assertEquals(UiText.StringResource(R.string.release_unofficial), flags.first().label)
        assertEquals(
            UiText.StringResource(R.string.detail_age_all),
            release().copy(minAge = 0).toHeroUIModel().flags.last().label,
        )
    }

    @Test
    fun `hero image prefers the package front`() {
        val hero = release(
            images = listOf(
                ReleaseImage(ReleaseImageType.PackageBack, cover("back")),
                ReleaseImage(ReleaseImageType.PackageFront, cover("front", sexual = 2.0)),
            ),
        ).toHeroUIModel()

        assertEquals("front", hero.imageUrl)
        assertEquals(true, hero.isImageSensitive)
        assertNull(release().toHeroUIModel().imageUrl)
    }
}
