package com.zc.vndb.feature.discover.presentation.component.trendingrelease.mapper

import com.zc.vndb.common.domain.model.ReleaseDate
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.discover.domain.model.Platform
import com.zc.vndb.feature.discover.domain.model.ReleaseKind
import com.zc.vndb.feature.discover.domain.model.TrendingRelease
import com.zc.vndb.feature.discover.presentation.component.trendingrelease.model.ReleaseKindTone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth

class TrendingReleaseUIModelMapperTest {

    private val release = TrendingRelease(
        releaseId = "r1",
        novelId = "v1",
        title = "ATRI -My Dear Moments-",
        kind = ReleaseKind.NewTranslation,
        releaseDate = ReleaseDate.Full(LocalDate(2024, 10, 5)),
        platforms = listOf(Platform("win"), Platform("swi"), Platform("ps4")),
        rating = 81.2,
        cover = null,
    )

    @Test
    fun `maps label, tone, date, rating and the first two platforms`() {
        val model = release.toUIModel()

        assertEquals(
            UiText.StringResource(StringResource.discover_release_translation),
            model.kindLabel,
        )
        assertEquals(ReleaseKindTone.Translation, model.kindTone)
        assertEquals(UiText.StringValue("Oct 2024"), model.dateLabel)
        assertEquals(UiText.StringValue("8.12"), model.ratingLabel)
        assertEquals(
            listOf<UiText>(UiText.StringValue("Windows"), UiText.StringValue("Switch")),
            model.platforms,
        )
    }

    @Test
    fun `every kind has its own label and tone`() {
        val expected = mapOf(
            ReleaseKind.NewRelease to (StringResource.discover_release_new to ReleaseKindTone.New),
            ReleaseKind.NewTranslation to
                (StringResource.discover_release_translation to ReleaseKindTone.Translation),
            ReleaseKind.RecentPatch to
                (StringResource.discover_release_patch to ReleaseKindTone.Patch),
            ReleaseKind.ReRelease to
                (StringResource.discover_release_rerelease to ReleaseKindTone.Rerelease),
        )
        expected.forEach { (kind, labelAndTone) ->
            val model = release.copy(kind = kind).toUIModel()
            assertEquals(UiText.StringResource(labelAndTone.first), model.kindLabel)
            assertEquals(labelAndTone.second, model.kindTone)
        }
    }

    @Test
    fun `partial and unknown dates`() {
        assertEquals(
            UiText.StringValue("Mar 2021"),
            ReleaseDate.Month(YearMonth(2021, 3)).toLabel(),
        )
        assertEquals(UiText.StringValue("2021"), ReleaseDate.Year(2021).toLabel())
        assertEquals(UiText.StringResource(StringResource.release_tba), ReleaseDate.Tba.toLabel())
    }

    @Test
    fun `unknown platform codes are shown upper-cased`() {
        assertEquals(
            listOf<UiText>(UiText.StringValue("FMT")),
            release.copy(platforms = listOf(Platform("fmt"))).toUIModel().platforms,
        )
    }
}
