package com.zc.vndb.feature.detail.domain.model

import com.zc.vndb.common.domain.model.ReleaseDate

data class RelatedNovel(
    val id: String,
    val title: String,
    val relation: RelationType,
    val isOfficial: Boolean,
    val releaseDate: ReleaseDate,
    val rating: Double?,
    /** Title in the original script, when VNDB has one. */
    val originalTitle: String? = null,
)
