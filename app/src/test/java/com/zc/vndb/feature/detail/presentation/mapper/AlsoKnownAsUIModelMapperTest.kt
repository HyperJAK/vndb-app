package com.zc.vndb.feature.detail.presentation.mapper

import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.DetailSamples
import com.zc.vndb.feature.detail.domain.model.NovelTitle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AlsoKnownAsUIModelMapperTest {

    @Test
    fun `skips titles the hero already shows`() {
        val model = DetailSamples.novel(
            titles = listOf(
                NovelTitle("en", "STEINS;GATE", latin = null, isOfficial = true, isMain = false),
                NovelTitle("zh-Hans", "命运石之门", latin = null, isOfficial = true, isMain = false),
            ),
            aliases = listOf("SG"),
        ).toAlsoKnownAs()

        assertEquals(listOf<UiText>(UiText.StringValue("命运石之门")), model?.titles?.map { it.title })
        assertEquals(listOf<UiText>(UiText.StringValue("SG")), model?.aliases?.toList())
    }

    @Test
    fun `null when there is nothing else`() {
        assertNull(DetailSamples.novel().toAlsoKnownAs())
    }
}
