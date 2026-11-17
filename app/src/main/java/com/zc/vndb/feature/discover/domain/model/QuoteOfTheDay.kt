package com.zc.vndb.feature.discover.domain.model

/** A quote VNDB readers submitted, with the visual novel (and character) it comes from. */
data class QuoteOfTheDay(
    val id: String,
    val text: String,
    val novelId: String,
    val novelTitle: String,
    /** The title in its original script, when it differs from [novelTitle]. */
    val novelOriginalTitle: String?,
    /** Null for a quote VNDB does not attribute to a character. */
    val characterName: String?,
)
