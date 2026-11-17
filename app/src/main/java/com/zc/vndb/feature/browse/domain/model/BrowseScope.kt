package com.zc.vndb.feature.browse.domain.model

/** What a Browse list is limited to: nothing (the Browse tab), one tag or one producer. */
sealed interface BrowseScope {
    data object None : BrowseScope
    data class Tag(val id: String) : BrowseScope
    data class Producer(val id: String) : BrowseScope

    /** The sort a list starts with. A producer's page opens on what they made most recently. */
    val defaultSort: BrowseSort
        get() = if (this is Producer) BrowseSort.Newest else BrowseSort.Popularity
}
