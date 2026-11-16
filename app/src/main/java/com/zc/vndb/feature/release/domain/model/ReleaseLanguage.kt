package com.zc.vndb.feature.release.domain.model

data class ReleaseLanguage(
    /** VNDB language code, e.g. "en", "zh-Hans". */
    val code: String,
    /** Title in this language, when VNDB has one. */
    val title: String?,
    val isMain: Boolean,
    val isMachineTranslated: Boolean,
)
