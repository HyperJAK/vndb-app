package com.zc.vndb.feature.detail.domain.model

data class NovelEditions(
    /** One edition per platform, earliest first. */
    val editions: List<Edition>,
    val ageRange: AgeRange?,
)
