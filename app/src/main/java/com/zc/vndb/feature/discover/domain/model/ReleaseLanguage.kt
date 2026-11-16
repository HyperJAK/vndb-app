package com.zc.vndb.feature.discover.domain.model

data class ReleaseLanguage(
    /** VNDB language code, e.g. "en", "ja", "zh-Hans". */
    val code: String,
    val isMachineTranslated: Boolean,
)
