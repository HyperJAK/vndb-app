package com.zc.vndb.feature.browse.domain.model

data class BrowseFilter(
    val query: String = "",
    val ageRating: AgeRating = AgeRating.AllAges,
    val genres: Set<Genre> = emptySet(),
    val sort: BrowseSort = BrowseSort.Popularity,
    /** A tag or producer page limits the list to it, on top of [genres]. */
    val scope: BrowseScope = BrowseScope.None,
)
