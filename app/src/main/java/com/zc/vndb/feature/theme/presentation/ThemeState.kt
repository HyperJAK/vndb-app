package com.zc.vndb.feature.theme.presentation

import androidx.compose.runtime.Immutable
import com.zc.vndb.feature.theme.presentation.component.themepreview.model.ThemeUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ThemeState(
    /** Empty until the stored settings have been read. */
    val themes: ImmutableList<ThemeUIModel> = persistentListOf(),
)
