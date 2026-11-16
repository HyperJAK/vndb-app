package com.zc.vndb.feature.release.domain.model

/** A physical or digital medium; [quantity] is 0 for downloads. */
data class ReleaseMedium(
    /** VNDB medium code, e.g. "dvd", "blr", "in". */
    val code: String,
    val quantity: Int,
)
