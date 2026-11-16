package com.zc.vndb.feature.release.domain.model

import com.zc.vndb.common.domain.model.Cover
import com.zc.vndb.common.domain.model.ReleaseDate

/** A VN this release belongs to. */
data class ReleaseNovel(
    val id: String,
    val title: String,
    val releaseDate: ReleaseDate,
    val cover: Cover?,
    val type: ReleaseType,
    /** Title in the original script, when VNDB has one. */
    val originalTitle: String? = null,
)
