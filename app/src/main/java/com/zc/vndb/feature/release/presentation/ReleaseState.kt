package com.zc.vndb.feature.release.presentation

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.component.error.model.ErrorUIModel
import com.zc.vndb.common.ui.component.linksrow.model.LinkUIModel
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.release.presentation.component.releasehero.model.ReleaseHeroUIModel
import com.zc.vndb.feature.release.presentation.component.releasenovelrow.model.ReleaseNovelRowUIModel
import com.zc.vndb.feature.release.presentation.model.ReleaseImageUIModel
import com.zc.vndb.feature.release.presentation.model.ReleaseLanguageUIModel
import com.zc.vndb.feature.release.presentation.model.ReleaseProducerUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ReleaseState(
    val releaseId: String = "",
    val isLoading: Boolean = false,
    val error: ErrorUIModel? = null,
    val hero: ReleaseHeroUIModel? = null,
    val links: ImmutableList<LinkUIModel> = persistentListOf(),
    val novels: ImmutableList<ReleaseNovelRowUIModel> = persistentListOf(),
    val languages: ImmutableList<ReleaseLanguageUIModel> = persistentListOf(),
    val producers: ImmutableList<ReleaseProducerUIModel> = persistentListOf(),
    val images: ImmutableList<ReleaseImageUIModel> = persistentListOf(),
    val notes: UiText? = null,
    val isNotesExpanded: Boolean = false,
)
