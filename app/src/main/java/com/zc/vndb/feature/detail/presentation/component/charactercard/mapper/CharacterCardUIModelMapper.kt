package com.zc.vndb.feature.detail.presentation.component.charactercard.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.CharacterRole
import com.zc.vndb.feature.detail.domain.model.NovelCharacter
import com.zc.vndb.feature.detail.presentation.component.charactercard.model.CharacterCardUIModel

fun NovelCharacter.toCardUIModel(): CharacterCardUIModel = CharacterCardUIModel(
    id = id,
    name = UiText.StringValue(name),
    originalName = originalName?.let(UiText::StringValue),
    roleLabel = UiText.StringResource(
        if (role == CharacterRole.Main) R.string.detail_role_main else R.string.detail_role_primary,
    ),
    isMain = role == CharacterRole.Main,
    voiceActorLabel = voiceActor?.let { UiText.StringResource(R.string.detail_cv, listOf(it)) },
    imageUrl = image?.url,
    isImageSensitive = image?.isSensitive ?: false,
)
