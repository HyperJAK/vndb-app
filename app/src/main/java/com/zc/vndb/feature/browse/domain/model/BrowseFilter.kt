package com.zc.vndb.feature.browse.domain.model

data class BrowseFilter(
    val query: String = "",
    val ageRating: AgeRating = AgeRating.AllAges,
    val genres: Set<Genre> = emptySet(),
    val sort: BrowseSort = BrowseSort.Popularity,
    /** Any VNDB tag the list is scoped to (a tag page), on top of [genres]. */
    val tagId: String? = null,
)
