package com.zc.vndb.feature.browse.domain.model

enum class BrowseSort(
    /** Popularity, rating and newest start with the highest; relevance and title with the first. */
    val isNaturallyDescending: Boolean,
) {
    /** Best text match; only meaningful with a search query. */
    Relevance(isNaturallyDescending = false),
    Popularity(isNaturallyDescending = true),
    Rating(isNaturallyDescending = true),
    Newest(isNaturallyDescending = true),
    Title(isNaturallyDescending = false),
}
