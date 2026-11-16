package com.zc.vndb.common.domain.model

data class NovelTag(
    /** VNDB tag id, e.g. "g105". */
    val id: String,
    val name: String,
    /** Tag relevance, 0–3. */
    val rating: Double,
    /** 0 = no spoiler, 1 = minor, 2 = major. */
    val spoilerLevel: Int,
    val category: TagCategory,
)
