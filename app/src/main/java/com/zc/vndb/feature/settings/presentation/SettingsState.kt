package com.zc.vndb.feature.settings.presentation

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.settings.presentation.model.SettingOptionUIModel
import com.zc.vndb.feature.settings.presentation.model.SettingsDialog
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class SettingsState(
    /** False until the stored settings have been read. */
    val isLoaded: Boolean = false,
    val titleLanguageOptions: ImmutableList<SettingOptionUIModel> = persistentListOf(),
    val titleLanguageLabel: UiText? = null,
    val spoilerOptions: ImmutableList<SettingOptionUIModel> = persistentListOf(),
    val spoilerLabel: UiText? = null,
    val spoilIfFinished: Boolean = false,
    /** "Spoil me if finished" needs the user's lists. */
    val isSignedIn: Boolean = false,
    val showSensitiveContent: Boolean = false,
    val includeAdultInBrowse: Boolean = false,
    val languageOptions: ImmutableList<SettingOptionUIModel> = persistentListOf(),
    val languagesLabel: UiText? = null,
    /** "142 MB"; null while measuring. */
    val imageCacheLabel: UiText? = null,
    val isClearingCache: Boolean = false,
    val versionLabel: UiText? = null,
    val dialog: SettingsDialog? = null,
)
