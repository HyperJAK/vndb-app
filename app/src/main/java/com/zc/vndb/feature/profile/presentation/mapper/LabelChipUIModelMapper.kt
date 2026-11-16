package com.zc.vndb.feature.profile.presentation.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.profile.domain.model.ListLabel
import com.zc.vndb.feature.profile.presentation.model.LabelChipUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun List<ListLabel>.toChips(selectedId: Int?): ImmutableList<LabelChipUIModel> = map { label ->
    LabelChipUIModel(
        id = label.id,
        label = UiText.StringResource(
            R.string.profile_label_with_count,
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
