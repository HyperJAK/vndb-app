package com.zc.vndb.feature.profile.presentation.component.profileheader.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.profile.domain.model.Session
import com.zc.vndb.feature.profile.domain.model.UserProfile
import com.zc.vndb.feature.profile.presentation.component.profileheader.model.ProfileHeaderUIModel
import kotlin.math.roundToInt

private const val MINUTES_PER_HOUR = 60.0

/** The user card; [user] adds the play-time line once loaded. */
fun Session.toHeaderUIModel(user: UserProfile? = null) = ProfileHeaderUIModel(
    username = UiText.StringValue(username),
    initial = UiText.StringValue(username.firstOrNull()?.uppercase().orEmpty()),
    userId = UiText.StringValue(userId),
    statsLabel = user?.takeIf { it.lengthVotes > 0 }?.toStatsLabel(),
    privateNote = if (canReadPrivateList) {
        null
    } else {
        UiText.StringResource(
            R.string.profile_private_hidden,
        )
    },
)

private fun UserProfile.toStatsLabel(): UiText = UiText.StringResource(
    R.string.profile_stats_line,
    listOf(
        UiText.StringPlural(
            R.plurals.profile_length_votes,
            lengthVotes,
            listOf("%,d".format(lengthVotes)),
        ),
        UiText.StringResource(
            R.string.profile_hours_logged,
            listOf("%,d".format((lengthVotesMinutes / MINUTES_PER_HOUR).roundToInt())),
        ),
    ),
)
