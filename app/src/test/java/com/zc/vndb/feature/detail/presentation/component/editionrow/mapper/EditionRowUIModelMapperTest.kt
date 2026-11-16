package com.zc.vndb.feature.detail.presentation.component.editionrow.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.DetailSamples.edition
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class EditionRowUIModelMapperTest {

    @Test
    fun `maps platform, languages, date and family`() {
        val row = edition(
            "r1",
            platform = "xbo",
        ).copy(languageCodes = listOf("en", "ja")).toRowUIModel()

        assertEquals("r1-xbo", row.key)
        assertEquals("r1", row.releaseId)
        assertEquals(UiText.StringValue("Xbox One"), row.platformLabel)
        assertEquals(UiText.StringValue("2010-01-01"), row.dateLabel)
        assertEquals(UiText.StringResource(R.string.detail_family_pc), row.familyLabel)
        assertEquals(2, (row.languagesLabel as UiText.StringValue).value.split(", ").size)
        assertNull(edition("r2").copy(languageCodes = emptyList()).toRowUIModel().languagesLabel)
    }
}
