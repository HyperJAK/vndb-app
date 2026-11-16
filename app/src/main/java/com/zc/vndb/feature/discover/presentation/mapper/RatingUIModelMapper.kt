package com.zc.vndb.feature.discover.presentation.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.text.UiText
import kotlin.math.roundToInt

private const val HUNDREDTHS_PER_POINT = 100
private const val VNDB_TO_HUNDREDTHS = 10

/** VNDB's 10–100 rating as a score out of 10 with two decimals: 89.2 → "8.92". Always uses '.'. */
fun Double.toRatingLabel(): UiText = UiText.StringValue(formatRating())

/** "8.92 / 10", as shown on the featured novel. */
fun Double.toRatingOutOfTenLabel(): UiText = UiText.StringResource(
    R.string.rating_out_of_ten,
    listOf(formatRating()),
)

private fun Double.formatRating(): String {
    val hundredths = (this * VNDB_TO_HUNDREDTHS).roundToInt()
    val whole = hundredths / HUNDREDTHS_PER_POINT
    val fraction = (hundredths % HUNDREDTHS_PER_POINT).toString().padStart(2, '0')
    return "$whole.$fraction"
}
