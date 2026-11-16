package com.zc.vndb.feature.discover.domain.model

/** One VNDB release (an edition of one or more visual novels on some platforms and languages). */
data class Release(
    val id: String,
    val releaseDate: ReleaseDate,
    val isPatch: Boolean,
    val languages: List<ReleaseLanguage>,
    val platforms: List<Platform>,
    val novels: List<ReleaseNovel>,
)
