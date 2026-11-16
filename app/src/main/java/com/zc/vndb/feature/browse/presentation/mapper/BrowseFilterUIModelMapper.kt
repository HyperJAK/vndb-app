package com.zc.vndb.feature.browse.presentation.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.browse.domain.model.BrowseFilter
import com.zc.vndb.feature.browse.domain.model.BrowseSort
import com.zc.vndb.feature.browse.domain.model.Genre
import com.zc.vndb.feature.browse.presentation.model.GenreChipUIState
import com.zc.vndb.feature.browse.presentation.model.SortOptionUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/** Sort choices for the menu; Relevance only makes sense while searching. */
fun BrowseFilter.toSortOptions(): ImmutableList<SortOptionUIModel> = BrowseSort.entries
    .filter { it != BrowseSort.Relevance || query.isNotBlank() }
    .map { SortOptionUIModel(sort = it, label = it.toLabel()) }
    .toImmutableList()

fun BrowseFilter.toGenreChips(): ImmutableList<GenreChipUIState> = Genre.entries
    .map {
        GenreChipUIState(
            genre = it,
            label = UiText.StringResource(it.labelRes()),
            isSelected = it in genres,
        )
    }
    .toImmutableList()

fun BrowseSort.toLabel(): UiText = UiText.StringResource(
    when (this) {
        BrowseSort.Relevance -> R.string.browse_sort_relevance
        BrowseSort.Popularity -> R.string.browse_sort_popularity
        BrowseSort.Rating -> R.string.browse_sort_rating
        BrowseSort.Newest -> R.string.browse_sort_newest
        BrowseSort.Title -> R.string.browse_sort_title
    },
)

/** "Found 142 titles", grouped for the device locale. */
fun Int.toResultCountLabel(): UiText = UiText.StringPlural(
    R.plurals.browse_found_titles,
    this,
    listOf("%,d".format(this)),
)

private fun Genre.labelRes(): StringResource = when (this) {
    Genre.Romance -> R.string.browse_genre_romance
    Genre.Mystery -> R.string.browse_genre_mystery
    Genre.SciFi -> R.string.browse_genre_scifi
    Genre.Horror -> R.string.browse_genre_horror
    Genre.Psychological -> R.string.browse_genre_psychological
    Genre.Fantasy -> R.string.browse_genre_fantasy
    Genre.Drama -> R.string.browse_genre_drama
    Genre.Comedy -> R.string.browse_genre_comedy
    Genre.Action -> R.string.browse_genre_action
}
