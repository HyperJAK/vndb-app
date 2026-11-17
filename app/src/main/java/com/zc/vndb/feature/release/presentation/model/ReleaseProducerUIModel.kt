package com.zc.vndb.feature.release.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.text.UiText
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class ReleaseProducerUIModel(
    val id: String,
    val name: UiText,
    val roles: ImmutableList<UiText>,
)
