package com.zc.vndb.feature.discover.domain.model

/** A visual novel as shown in Discover lists (Top Rated row, featured pool). */
data class DiscoverNovel(
    val id: String,
    val title: String,
    /** Bayesian rating on VNDB's 10–100 scale; null when the VN has too few votes. */
    val rating: Double?,
    val cover: Cover?,
    val developerName: String?,
)
