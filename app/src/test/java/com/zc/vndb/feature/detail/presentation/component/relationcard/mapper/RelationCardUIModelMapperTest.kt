package com.zc.vndb.feature.detail.presentation.component.relationcard.mapper

import com.zc.vndb.common.domain.model.ReleaseDate
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.DetailSamples.relation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RelationCardUIModelMapperTest {

    @Test
    fun `maps relation label, year and score`() {
        val card = relation("v17102").toCardUIModel()

        assertEquals(
            UiText.StringResource(StringResource.detail_relation_side_story),
            card.relationLabel,
        )
        assertEquals(UiText.StringValue("2015"), card.yearLabel)
        assertEquals(
            UiText.StringResource(StringResource.detail_relation_score, listOf("8.08")),
            card.scoreLabel,
        )
    }

    @Test
    fun `unknown date and rating`() {
        val card = relation("v1", released = ReleaseDate.Tba).copy(rating = null).toCardUIModel()

        assertEquals(UiText.StringResource(StringResource.release_tba), card.yearLabel)
        assertNull(card.scoreLabel)
    }
}
