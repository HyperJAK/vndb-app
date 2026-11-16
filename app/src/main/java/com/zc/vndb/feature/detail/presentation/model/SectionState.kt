package com.zc.vndb.feature.detail.presentation.model

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.component.error.model.ErrorUIModel

/** A lazily loaded section: nothing requested yet, in flight, ready, or failed (with its own Retry). */
@Immutable
sealed interface SectionState<out T> {
    data object Idle : SectionState<Nothing>
    data object Loading : SectionState<Nothing>
    data class Loaded<T>(val data: T) : SectionState<T>
    data class Failed(val error: ErrorUIModel) : SectionState<Nothing>
}

val <T> SectionState<T>.dataOrNull: T?
    get() = (this as? SectionState.Loaded<T>)?.data
