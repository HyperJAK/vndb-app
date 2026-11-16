package com.zc.vndb.feature.browse.domain.model

import com.zc.vndb.common.domain.model.TagCategory

/** A VNDB tag, for the header of a tag-filtered Browse. */
data class TagInfo(
    val id: String,
    val name: String,
    val aliases: List<String>,
    /** VNDB BBCode. */
    val description: String?,
    val category: TagCategory?,
    /** How many VNs VNDB lists under the tag (its own count, before Browse's filters). */
    val novelCount: Int,
)
