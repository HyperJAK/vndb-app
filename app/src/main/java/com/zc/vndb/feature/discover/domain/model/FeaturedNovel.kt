package com.zc.vndb.feature.discover.domain.model

import com.zc.vndb.common.domain.model.Cover

/** The "VN of the Week" hero. */
data class FeaturedNovel(
    val id: String,
    val title: String,
    val rating: Double?,
    val cover: Cover?,
    val description: String?,
    val developerName: String?,
    val scenarioWriter: String?,
    val tags: List<String>,
    /** Guessed from the cover flags and tags; VNDB only has age ratings per release. */
    val isAllAges: Boolean,
    /** Title in the original script, when VNDB has one. */
    val originalTitle: String? = null,
)
