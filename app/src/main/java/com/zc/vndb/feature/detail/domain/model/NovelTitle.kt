package com.zc.vndb.feature.detail.domain.model

/** The VN's title in one language. */
data class NovelTitle(
    /** VNDB language code, e.g. "ja", "zh-Hans". */
    val languageCode: String,
    val title: String,
    /** Romanised title, when the original script is not Latin. */
    val latin: String?,
    val isOfficial: Boolean,
    /** The title in the VN's original language. */
    val isMain: Boolean,
)
