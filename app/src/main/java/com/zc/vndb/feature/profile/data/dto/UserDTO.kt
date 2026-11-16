package com.zc.vndb.feature.profile.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** One user from `GET /user` (the response maps each `q` to a user or null). */
@Serializable
data class UserDTO(
    @SerialName("id") val id: String,
    @SerialName("username") val username: String,
    @SerialName("lengthvotes") val lengthVotes: Int?,
    @SerialName("lengthvotes_sum") val lengthVotesSum: Int?,
)
