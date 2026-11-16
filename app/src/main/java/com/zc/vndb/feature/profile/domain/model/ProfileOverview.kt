package com.zc.vndb.feature.profile.domain.model

/** The signed-in user's profile and their non-empty list labels. */
data class ProfileOverview(
    /** Null when `GET /user` failed; the lists still show. */
    val user: UserProfile?,
    val labels: List<ListLabel>,
)
