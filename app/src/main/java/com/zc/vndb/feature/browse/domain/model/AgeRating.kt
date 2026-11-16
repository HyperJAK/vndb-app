package com.zc.vndb.feature.browse.domain.model

/** Exclusive age filter. VNDB rates releases, not VNs: [Adult] = has an 18+ release, [AllAges] = has none. */
enum class AgeRating {
    AllAges,
    Adult,
}
