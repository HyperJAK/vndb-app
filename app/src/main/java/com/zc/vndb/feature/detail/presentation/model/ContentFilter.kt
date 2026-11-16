package com.zc.vndb.feature.detail.presentation.model

/** Screenshot toggles, as on vndb.org: three sexual and three violence levels. */
enum class ContentFilter(val isSexual: Boolean, val level: Int) {
    Safe(isSexual = true, level = 0),
    Suggestive(isSexual = true, level = 1),
    Explicit(isSexual = true, level = 2),
    Tame(isSexual = false, level = 0),
    Violent(isSexual = false, level = 1),
    Brutal(isSexual = false, level = 2),
    ;

    companion object {
        val Default: Set<ContentFilter> = setOf(Safe, Tame)
    }
}
