package com.zc.vndb.feature.release.presentation.component.releasenovelrow.mapper

import com.zc.vndb.common.domain.model.TitleLanguage
import com.zc.vndb.common.ui.mapper.primaryTitle
import com.zc.vndb.common.ui.mapper.toYearLabel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.release.domain.model.ReleaseNovel
import com.zc.vndb.feature.release.domain.model.ReleaseType
import com.zc.vndb.feature.release.presentation.component.releasenovelrow.model.ReleaseNovelRowUIModel

fun ReleaseNovel.toRowUIModel(
    titleLanguage: TitleLanguage = TitleLanguage.Romanized,
): ReleaseNovelRowUIModel = ReleaseNovelRowUIModel(
    id = id,
    title = primaryTitle(title, originalTitle, titleLanguage),
    yearLabel = releaseDate.toYearLabel(),
    typeLabel = UiText.StringResource(
        when (type) {
            ReleaseType.Complete -> StringResource.release_type_complete
            ReleaseType.Partial -> StringResource.release_type_partial
            ReleaseType.Trial -> StringResource.release_type_trial
        },
    ),
    isComplete = type == ReleaseType.Complete,
    thumbnailUrl = cover?.thumbnailUrl ?: cover?.url,
    isCoverSensitive = cover?.isSensitive ?: false,
)
