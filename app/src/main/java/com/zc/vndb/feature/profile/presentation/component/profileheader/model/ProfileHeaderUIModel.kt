package com.zc.vndb.feature.profile.presentation.component.profileheader.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText

/** The user card at the top of the signed-in profile. */
@Immutable
data class ProfileHeaderUIModel(
    val username: UiText,
    /** First letter of the username, for the avatar. */
    val initial: UiText,
    val userId: UiText,
    /** "12 play-time votes · 170h logged", once known. */
    val statsLabel: UiText? = null,
    /** Shown when the token cannot read private lists. */
    val privateNote: UiText? = null,
)
