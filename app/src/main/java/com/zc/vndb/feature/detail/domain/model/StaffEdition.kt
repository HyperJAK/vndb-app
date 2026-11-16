package com.zc.vndb.feature.detail.domain.model

/** A group of staff credits beyond the original, e.g. a translation ("NekoNyan's translation"). */
data class StaffEdition(
    val id: Int,
    val name: String,
    val languageCode: String?,
    val isOfficial: Boolean,
)
