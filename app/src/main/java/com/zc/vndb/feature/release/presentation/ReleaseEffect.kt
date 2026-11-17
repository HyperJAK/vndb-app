package com.zc.vndb.feature.release.presentation

import com.zc.vndb.common.ui.component.error.model.ErrorUIModel

sealed interface ReleaseEffect {
    data object NavigateBack : ReleaseEffect
    data class Share(val title: String, val url: String) : ReleaseEffect
    data class OpenUrl(val url: String) : ReleaseEffect
    data class NavigateToNovel(val id: String) : ReleaseEffect
    data class NavigateToProducer(val id: String) : ReleaseEffect
    data class ShowError(val error: ErrorUIModel) : ReleaseEffect
}
