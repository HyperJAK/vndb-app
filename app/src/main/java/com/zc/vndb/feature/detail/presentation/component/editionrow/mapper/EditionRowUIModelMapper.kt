package com.zc.vndb.feature.detail.presentation.component.editionrow.mapper

import com.zc.vndb.common.ui.mapper.platformLabel
import com.zc.vndb.common.ui.mapper.toIsoLabel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.Edition
import com.zc.vndb.feature.detail.domain.model.PlatformFamily
import com.zc.vndb.feature.detail.presentation.component.editionrow.model.EditionRowUIModel
import java.util.Locale

private const val MAX_LANGUAGES = 3

fun Edition.toRowUIModel(): EditionRowUIModel = EditionRowUIModel(
    key = "$id-$platform",
    releaseId = id,
    title = UiText.StringValue(title),
    platformLabel = platformLabel(platform),
    languagesLabel = languageCodes.takeIf { it.isNotEmpty() }?.let { codes ->
        UiText.StringValue(codes.take(MAX_LANGUAGES).joinToString(", ") { it.toLanguageName() })
    },
    dateLabel = releaseDate.toIsoLabel(),
    family = family,
    familyLabel = family.toLabel(),
)

/** "en" → "English", "zh-Hans" → "Chinese", in the device language. */
private fun String.toLanguageName(): String =
    Locale.forLanguageTag(this).displayLanguage.ifBlank { this }

private fun PlatformFamily.toLabel(): UiText = UiText.StringResource(
    when (this) {
        PlatformFamily.Pc -> StringResource.detail_family_pc
        PlatformFamily.Console -> StringResource.detail_family_console
        PlatformFamily.Mobile -> StringResource.detail_family_mobile
        PlatformFamily.Web -> StringResource.detail_family_web
        PlatformFamily.Other -> StringResource.detail_family_other
    },
)
