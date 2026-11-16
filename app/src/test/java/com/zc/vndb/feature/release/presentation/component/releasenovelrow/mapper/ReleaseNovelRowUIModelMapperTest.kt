package com.zc.vndb.feature.release.presentation.component.releasenovelrow.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.release.domain.model.ReleaseSamples.novel
import com.zc.vndb.feature.release.domain.model.ReleaseType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ReleaseNovelRowUIModelMapperTest {

    @Test
    fun `maps type, year and thumbnail`() {
        val row = novel("v2002", year = 2009, type = ReleaseType.Trial).toRowUIModel()

        assertEquals(UiText.StringResource(R.string.release_type_trial), row.typeLabel)
        assertFalse(row.isComplete)
        assertEquals(UiText.StringValue("2009"), row.yearLabel)
        assertEquals("cover-v2002-thumb", row.thumbnailUrl)
    }
}
