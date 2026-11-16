package com.zc.vndb.feature.character.presentation.component.appearancerow.mapper

import com.zc.vndb.common.domain.model.CharacterRole
import com.zc.vndb.common.domain.model.TitleLanguage
import com.zc.vndb.common.ui.component.ratingbadge.mapper.toRatingLabel
import com.zc.vndb.common.ui.mapper.primaryTitle
import com.zc.vndb.common.ui.mapper.toYearLabel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.character.domain.model.CharacterAppearance
import com.zc.vndb.feature.character.presentation.component.appearancerow.model.AppearanceRowUIModel

fun CharacterAppearance.toRowUIModel(
    titleLanguage: TitleLanguage = TitleLanguage.Romanized,
): AppearanceRowUIModel = AppearanceRowUIModel(
    novelId = novelId,
    title = primaryTitle(novelTitle, novelOriginalTitle, titleLanguage),
    yearLabel = releaseDate.toYearLabel(),
    roleLabel = role.toLabel(),
    isMajorRole = role == CharacterRole.Main || role == CharacterRole.Primary,
    voiceActorLabel = voiceActor?.let {
        UiText.StringResource(
            StringResource.detail_cv,
            listOf(it),
        )
    },
    ratingLabel = rating?.toRatingLabel(),
    thumbnailUrl = cover?.thumbnailUrl ?: cover?.url,
    isCoverSensitive = cover?.isSensitive ?: false,
)

private fun CharacterRole.toLabel(): UiText = UiText.StringResource(
    when (this) {
        CharacterRole.Main -> StringResource.character_role_main
        CharacterRole.Primary -> StringResource.character_role_primary
        CharacterRole.Side -> StringResource.character_role_side
        CharacterRole.Appears -> StringResource.character_role_appears
    },
)
