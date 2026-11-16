package com.zc.vndb.feature.discover.domain.model

/** The full record of one visual novel, as needed to build a [FeaturedNovel]. */
data class NovelDetails(
    val id: String,
    val title: String,
    val rating: Double?,
    val cover: Cover?,
    val description: String?,
    val developerNames: List<String>,
    val staff: List<StaffCredit>,
    val tags: List<NovelTag>,
)
