package com.zc.vndb.common.ui.mapper

import com.zc.vndb.common.domain.model.ReleaseDate
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText

/** "2009-10-15", "2009-10", "2009" or TBA, as VNDB writes them. */
fun ReleaseDate.toIsoLabel(): UiText = when (this) {
    is ReleaseDate.Full -> UiText.StringValue(date.toString())
    is ReleaseDate.Month -> UiText.StringValue(yearMonth.toString())
    is ReleaseDate.Year -> UiText.StringValue(year.toString())
    ReleaseDate.Tba -> UiText.StringResource(StringResource.release_tba)
}

/** Just the year, or TBA. */
fun ReleaseDate.toYearLabel(): UiText = when (this) {
    is ReleaseDate.Full -> UiText.StringValue(date.year.toString())
    is ReleaseDate.Month -> UiText.StringValue(yearMonth.year.toString())
    is ReleaseDate.Year -> UiText.StringValue(year.toString())
    ReleaseDate.Tba -> UiText.StringResource(StringResource.release_tba)
}
