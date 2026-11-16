package com.zc.vndb.feature.profile.presentation.mapper

import com.zc.vndb.common.domain.model.ListSort
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.profile.domain.model.ListLabel
import com.zc.vndb.feature.profile.presentation.model.LabelChipUIModel
import com.zc.vndb.feature.profile.presentation.model.SortOptionUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun List<ListLabel>.toChips(selectedId: Int?): ImmutableList<LabelChipUIModel> = map { label ->
    LabelChipUIModel(
        id = label.id,
        label = UiText.StringResource(
            StringResource.profile_label_with_count,
            listOf(label.name, "%,d".format(label.count)),
        ),
        isSelected = label.id == selectedId,
    )
}.toImmutableList()

/** Keeps [current] when it still exists, else Playing, else the first label. */
fun List<ListLabel>.pickSelected(current: Int?): Int? =
    current?.takeIf { id -> any { it.id == id } }
        ?: firstOrNull { it.id == ListLabel.PLAYING }?.id
        ?: firstOrNull()?.id

/** The sort menu, with [this] selected. */
fun ListSort.toSortOptions(): ImmutableList<SortOptionUIModel> = ListSort.entries.map { sort ->
    SortOptionUIModel(
        sort = sort,
        label = UiText.StringResource(
            when (sort) {
                ListSort.Title -> StringResource.profile_sort_title
                ListSort.Updated -> StringResource.profile_sort_updated
                ListSort.Vote -> StringResource.profile_sort_vote
            },
        ),
        isSelected = sort == this,
    )
}.toImmutableList()
