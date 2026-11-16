package com.zc.vndb.common.domain.model

/**
 * How the Profile lists are ordered; chosen on the Profile tab and kept in the settings. Each comment
 * gives the natural direction; `AppSettings.isListSortReversed` flips it.
 */
enum class ListSort {
    /** A to Z. */
    Title,

    /** Most recently changed first. */
    Updated,

    /** The user's vote, highest first; entries without a vote come last, by title. */
    Vote,
}
