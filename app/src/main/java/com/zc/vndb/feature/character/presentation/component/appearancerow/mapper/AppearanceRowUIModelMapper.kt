package com.zc.vndb.feature.character.presentation.component.appearancerow.mapper

import com.zc.vndb.R
import com.zc.vndb.common.domain.model.CharacterRole
import com.zc.vndb.common.domain.model.ReleaseDate
import com.zc.vndb.common.ui.component.ratingbadge.mapper.toRatingLabel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.character.domain.model.CharacterAppearance
import com.zc.vndb.feature.character.presentation.component.appearancerow.model.AppearanceRowUIModel

fun CharacterAppearance.toRowUIModel(): AppearanceRowUIModel = AppearanceRowUIModel(
    novelId = novelId,
    title = UiText.StringValue(novelTitle),
    yearLabel = when (val date = releaseDate) {
        is ReleaseDate.Full -> UiText.StringValue(date.date.year.toString())
        is ReleaseDate.Month -> UiText.StringValue(date.yearMonth.year.toString())
        is ReleaseDate.Year -> UiText.StringValue(date.year.toString())
        ReleaseDate.Tba -> UiText.StringResource(R.string.release_tba)
    },
    roleLabel = UiText.StringResource(role.labelRes()),
    isMajorRole = role == CharacterRole.Main || role == CharacterRole.Primary,
    voiceActorLabel = voiceActor?.let { UiText.StringResource(R.string.detail_cv, listOf(it)) },
    ratingLabel = rating?.toRatingLabel(),
    thumbnailUrl = cover?.thumbnailUrl ?: cover?.url,
    isCoverSensitive = cover?.isSensitive ?: false,
)

private fun CharacterRole.labelRes(): StringResource = when (this) {
    CharacterRole.Main -> R.string.character_role_main
    CharacterRole.Primary -> R.string.character_role_primary
    CharacterRole.Side -> R.string.character_role_side
    CharacterRole.Appears -> R.string.character_role_appears
}
