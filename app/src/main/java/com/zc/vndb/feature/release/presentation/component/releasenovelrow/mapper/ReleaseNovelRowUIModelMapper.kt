package com.zc.vndb.feature.release.presentation.component.releasenovelrow.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.mapper.toYearLabel
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.release.domain.model.ReleaseNovel
import com.zc.vndb.feature.release.domain.model.ReleaseType
import com.zc.vndb.feature.release.presentation.component.releasenovelrow.model.ReleaseNovelRowUIModel

fun ReleaseNovel.toRowUIModel(): ReleaseNovelRowUIModel = ReleaseNovelRowUIModel(
    id = id,
    title = UiText.StringValue(title),
    yearLabel = releaseDate.toYearLabel(),
    typeLabel = UiText.StringResource(
        when (type) {
            ReleaseType.Complete -> R.string.release_type_complete
            ReleaseType.Partial -> R.string.release_type_partial
            ReleaseType.Trial -> R.string.release_type_trial
        },
    ),
    isComplete = type == ReleaseType.Complete,
    thumbnailUrl = cover?.thumbnailUrl ?: cover?.url,
    isCoverSensitive = cover?.isSensitive ?: false,
)
