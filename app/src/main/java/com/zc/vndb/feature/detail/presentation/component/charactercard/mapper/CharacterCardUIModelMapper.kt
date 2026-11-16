package com.zc.vndb.feature.detail.presentation.component.charactercard.mapper

import com.zc.vndb.common.domain.model.CharacterRole
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.NovelCharacter
import com.zc.vndb.feature.detail.presentation.component.charactercard.model.CharacterCardUIModel

fun NovelCharacter.toCardUIModel(): CharacterCardUIModel = CharacterCardUIModel(
    id = id,
    name = UiText.StringValue(name),
    originalName = originalName?.let(UiText::StringValue),
    roleLabel = UiText.StringResource(
        when (role) {
            CharacterRole.Main -> StringResource.detail_role_main
            CharacterRole.Primary -> StringResource.detail_role_primary
            CharacterRole.Side -> StringResource.detail_role_side
            CharacterRole.Appears -> StringResource.detail_role_appears
        },
    ),
    isMain = role == CharacterRole.Main,
    voiceActorLabel = voiceActor?.let {
        UiText.StringResource(
            StringResource.detail_cv,
            listOf(it),
        )
    },
    imageUrl = image?.url,
    isImageSensitive = image?.isSensitive ?: false,
)
