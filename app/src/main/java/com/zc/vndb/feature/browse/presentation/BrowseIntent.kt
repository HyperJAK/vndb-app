package com.zc.vndb.feature.browse.presentation

import com.zc.vndb.feature.browse.domain.model.AgeRating
import com.zc.vndb.feature.browse.domain.model.BrowseSort
import com.zc.vndb.feature.browse.domain.model.Genre

sealed interface BrowseIntent {
    data object Load : BrowseIntent
    data object Retry : BrowseIntent

    /** Pull to refresh: reloads the first page (and tag header) while the results stay on screen. */
    data object Refresh : BrowseIntent
    data class QueryChanged(val query: String) : BrowseIntent
    data object SearchSubmitted : BrowseIntent
    data object QueryCleared : BrowseIntent
    data class AgeRatingSelected(val ageRating: AgeRating) : BrowseIntent
    data class GenreToggled(val genre: Genre) : BrowseIntent
    data class SortSelected(val sort: BrowseSort) : BrowseIntent
    data object EndReached : BrowseIntent
    data object LoadMoreRetried : BrowseIntent
    data class NovelClicked(val id: String) : BrowseIntent
    data object BackClicked : BrowseIntent
    data object TagDescriptionToggled : BrowseIntent
}
