package com.zc.vndb.feature.browse.presentation

import androidx.compose.runtime.Immutable
import com.zc.vndb.common.ui.component.error.model.ErrorUIModel
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.browse.domain.model.AgeRating
import com.zc.vndb.feature.browse.domain.model.BrowseSort
import com.zc.vndb.feature.browse.presentation.component.browsenovelcard.model.BrowseNovelCardUIModel
import com.zc.vndb.feature.browse.presentation.component.tagheader.model.TagHeaderUIModel
import com.zc.vndb.feature.browse.presentation.model.GenreChipUIState
import com.zc.vndb.feature.browse.presentation.model.SortOptionUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class BrowseState(
    /** A tag page (opened from a VN) rather than the Browse tab: shows Back and the tag header. */
    val isTagPage: Boolean = false,
    val tagHeader: TagHeaderUIModel? = null,
    val isTagDescriptionExpanded: Boolean = false,
    /** What is typed in the search field (applied after a short pause). */
    val query: String = "",
    val ageRating: AgeRating = AgeRating.AllAges,
    val sort: BrowseSort = BrowseSort.Popularity,
    val sortOptions: ImmutableList<SortOptionUIModel> = persistentListOf(),
    val genres: ImmutableList<GenreChipUIState> = persistentListOf(),
    val results: ImmutableList<BrowseNovelCardUIModel> = persistentListOf(),
    val resultCountLabel: UiText? = null,
    /** First page in flight; results are replaced when it lands. */
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val canLoadMore: Boolean = false,
    val loadMoreFailed: Boolean = false,
    /** First page failed. */
    val error: ErrorUIModel? = null,
) {
    val showEmpty: Boolean get() = !isLoading && error == null && results.isEmpty()
}
