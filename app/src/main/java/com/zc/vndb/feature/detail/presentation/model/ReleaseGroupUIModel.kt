package com.zc.vndb.feature.detail.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.presentation.component.editionrow.model.EditionRowUIModel
import kotlinx.collections.immutable.ImmutableList

/** Releases in one language. */
@Immutable
data class ReleaseGroupUIModel(
    val languageCode: String,
    val title: UiText,
    val releases: ImmutableList<EditionRowUIModel>,
)
