package com.zc.vndb.feature.browse.presentation

import com.zc.vndb.common.ui.component.error.model.ErrorUIModel

sealed interface BrowseEffect {
    data class NavigateToNovel(val id: String) : BrowseEffect
    data object NavigateBack : BrowseEffect
    data class OpenUrl(val url: String) : BrowseEffect
    data class ShowError(val error: ErrorUIModel) : BrowseEffect
}
