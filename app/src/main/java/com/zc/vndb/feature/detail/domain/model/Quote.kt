package com.zc.vndb.feature.detail.domain.model

data class Quote(
    val id: String,
    val text: String,
    /** Net votes on VNDB. */
    val score: Int,
    val characterId: String?,
    val characterName: String?,
)
