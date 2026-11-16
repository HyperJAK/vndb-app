package com.zc.vndb.feature.discover.presentation.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.discover.domain.model.DatabaseStats
import kotlin.test.Test
import kotlin.test.assertEquals

class DatabaseStatsUIModelMapperTest {

    @Test
    fun `stats count is a grouped plural`() {
        assertEquals(
            UiText.StringPlural(
                R.plurals.discover_stats_novels,
                68053,
                listOf("%,d".format(68053)),
            ),
            DatabaseStats(novelCount = 68053).toCountLabel(),
        )
    }
}
