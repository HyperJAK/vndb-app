package com.zc.vndb.feature.browse.presentation.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.browse.domain.model.BrowseFilter
import com.zc.vndb.feature.browse.domain.model.BrowseSort
import com.zc.vndb.feature.browse.domain.model.Genre
import kotlin.test.Test
import kotlin.test.assertEquals

class BrowseFilterUIModelMapperTest {

    @Test
    fun `relevance is offered only while searching`() {
        assertEquals(
            listOf(BrowseSort.Popularity, BrowseSort.Rating, BrowseSort.Newest, BrowseSort.Title),
            BrowseFilter().toSortOptions().map { it.sort },
        )
        assertEquals(
            BrowseSort.entries,
            BrowseFilter(query = "steins").toSortOptions().map { it.sort },
        )
        assertEquals(
            UiText.StringResource(R.string.browse_sort_relevance),
            BrowseSort.Relevance.toLabel(),
        )
    }

    @Test
    fun `genre chips reflect the selection`() {
        val chips = BrowseFilter(genres = setOf(Genre.Horror)).toGenreChips()

        assertEquals(Genre.entries, chips.map { it.genre })
        assertEquals(listOf(Genre.Horror), chips.filter { it.isSelected }.map { it.genre })
        assertEquals(
            UiText.StringResource(R.string.browse_genre_scifi),
            chips.single { it.genre == Genre.SciFi }.label,
        )
    }

    @Test
    fun `result count is a grouped plural`() {
        assertEquals(
            UiText.StringPlural(R.plurals.browse_found_titles, 35880, listOf("%,d".format(35880))),
            35880.toResultCountLabel(),
        )
    }
}
