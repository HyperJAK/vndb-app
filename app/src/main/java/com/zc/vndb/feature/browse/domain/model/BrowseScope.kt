package com.zc.vndb.feature.browse.domain.model

/** What a Browse list is limited to: nothing (the Browse tab), one tag, producer or staff member. */
sealed interface BrowseScope {
    data object None : BrowseScope
    data class Tag(val id: String) : BrowseScope
    data class Producer(val id: String) : BrowseScope
    data class Staff(val id: String) : BrowseScope

    /** The sort a list starts with. A person's or studio's page opens on their latest work. */
    val defaultSort: BrowseSort
        get() = when (this) {
            is Producer, is Staff -> BrowseSort.Newest
            None, is Tag -> BrowseSort.Popularity
        }
}
