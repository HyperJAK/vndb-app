package com.zc.vndb.feature.profile.presentation

sealed interface ProfileIntent {
    data object LogInClicked : ProfileIntent
    data object SettingsClicked : ProfileIntent
    data object Retry : ProfileIntent

    /** Pull to refresh: labels, profile and the selected label's first page. */
    data object Refresh : ProfileIntent
    data class LabelSelected(val id: Int) : ProfileIntent
    data object EndReached : ProfileIntent
    data object LoadMoreRetried : ProfileIntent
    data object EntriesRetried : ProfileIntent
    data class EntryClicked(val novelId: String) : ProfileIntent
    data object LogOutClicked : ProfileIntent
    data object LogOutConfirmed : ProfileIntent
    data object LogOutDismissed : ProfileIntent
}
