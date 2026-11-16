package com.zc.vndb.feature.profile.domain.model

/** Public profile data from `GET /user`. */
data class UserProfile(
    val id: String,
    val username: String,
    /** Play-time votes submitted. */
    val lengthVotes: Int,
    /** Sum of those votes, in minutes. */
    val lengthVotesMinutes: Int,
)
