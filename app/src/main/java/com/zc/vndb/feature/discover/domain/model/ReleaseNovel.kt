package com.zc.vndb.feature.discover.domain.model

import com.zc.vndb.common.domain.model.Cover
import com.zc.vndb.common.domain.model.ReleaseDate

/** The visual novel a [Release] belongs to. */
data class ReleaseNovel(
    val id: String,
    val title: String,
    val originalLanguage: String,
    val releaseDate: ReleaseDate,
    val rating: Double?,
    val cover: Cover?,
    /** Title in the original script, when VNDB has one. */
    val originalTitle: String? = null,
)
