package com.zc.vndb.feature.browse.domain.model

data class BrowsePage(
    val novels: List<BrowseNovel>,
    /** Total matches; only requested (and present) for the first page. */
    val totalCount: Int?,
    val hasMore: Boolean,
)
