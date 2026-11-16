package com.zc.vndb.common.ui.mapper

import com.zc.vndb.common.domain.model.NovelLength
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText

/** "<2h", "2-10h", … "30-50h", ">50h". */
fun NovelLength.toLabel(): UiText = UiText.StringResource(
    when (this) {
        NovelLength.VeryShort -> StringResource.length_very_short
        NovelLength.Short -> StringResource.length_short
        NovelLength.Medium -> StringResource.length_medium
        NovelLength.Long -> StringResource.length_long
        NovelLength.VeryLong -> StringResource.length_very_long
    },
)

/** "Very Short", "Short", "Medium", "Long", "Very Long". */
fun NovelLength.toNameLabel(): UiText = UiText.StringResource(
    when (this) {
        NovelLength.VeryShort -> StringResource.length_name_very_short
        NovelLength.Short -> StringResource.length_name_short
        NovelLength.Medium -> StringResource.length_name_medium
        NovelLength.Long -> StringResource.length_name_long
        NovelLength.VeryLong -> StringResource.length_name_very_long
    },
)

/** "Very Long (>50h)": the category name with its range. */
fun NovelLength.toNameWithRangeLabel(): UiText =
    UiText.StringResource(StringResource.length_name_with_range, listOf(toNameLabel(), toLabel()))
