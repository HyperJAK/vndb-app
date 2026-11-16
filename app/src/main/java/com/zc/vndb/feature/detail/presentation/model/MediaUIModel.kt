package com.zc.vndb.feature.detail.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/** Screenshots after the content toggles. */
@Immutable
data class MediaUIModel(
    val totalCount: Int,
    val sexualToggles: ImmutableList<ContentToggleUIState> = persistentListOf(),
    val violenceToggles: ImmutableList<ContentToggleUIState> = persistentListOf(),
    val groups: ImmutableList<ScreenshotGroupUIModel> = persistentListOf(),
    /** The first few visible screenshots, for the Overview preview. */
    val preview: ImmutableList<MediaImageUIModel> = persistentListOf(),
    val visibleCount: Int = 0,
    val hiddenLabel: UiText? = null,
)
