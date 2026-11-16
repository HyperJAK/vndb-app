package com.zc.vndb.common.ui.mapper

import com.zc.vndb.R
import com.zc.vndb.common.domain.model.NovelLength
import com.zc.vndb.common.ui.text.UiText

/** "<2h", "2-10h", … "30-50h", ">50h". */
fun NovelLength.toLabel(): UiText = UiText.StringResource(
    when (this) {
        NovelLength.VeryShort -> R.string.length_very_short
        NovelLength.Short -> R.string.length_short
        NovelLength.Medium -> R.string.length_medium
        NovelLength.Long -> R.string.length_long
        NovelLength.VeryLong -> R.string.length_very_long
    },
)

/** "Very Short", "Short", "Medium", "Long", "Very Long". */
fun NovelLength.toNameLabel(): UiText = UiText.StringResource(
    when (this) {
        NovelLength.VeryShort -> R.string.length_name_very_short
        NovelLength.Short -> R.string.length_name_short
        NovelLength.Medium -> R.string.length_name_medium
        NovelLength.Long -> R.string.length_name_long
        NovelLength.VeryLong -> R.string.length_name_very_long
    },
)
