package com.zc.vndb.feature.browse.domain.model

/**
 * Age filter. VNDB rates releases, not VNs: [Adult] = has an 18+ release, [AllAges] = has none,
 * [Any] = no age filter (both).
 */
enum class AgeRating {
    AllAges,
    Adult,
    Any,
}
