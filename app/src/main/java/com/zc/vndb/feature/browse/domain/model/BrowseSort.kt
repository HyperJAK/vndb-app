package com.zc.vndb.feature.browse.domain.model

enum class BrowseSort {
    /** Best text match; only meaningful with a search query. */
    Relevance,
    Popularity,
    Rating,
    Newest,
    Title,
}
