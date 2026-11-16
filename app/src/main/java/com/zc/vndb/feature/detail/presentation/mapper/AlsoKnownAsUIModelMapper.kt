package com.zc.vndb.feature.detail.presentation.mapper

import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.NovelDetail
import com.zc.vndb.feature.detail.presentation.model.AlsoKnownAsUIModel
import com.zc.vndb.feature.detail.presentation.model.TitleLineUIModel
import java.util.Locale
import kotlinx.collections.immutable.toImmutableList

/** Titles in other languages (except those the hero already shows) and aliases; null when none. */
fun NovelDetail.toAlsoKnownAs(): AlsoKnownAsUIModel? {
    val shown = setOf(title, originalTitle)
    val titleLines = titles.filter { it.title !in shown }.map {
        TitleLineUIModel(
            language = UiText.StringValue(
                Locale.forLanguageTag(it.languageCode).displayLanguage.ifBlank { it.languageCode },
            ),
            title = UiText.StringValue(it.title),
            latin = it.latin?.let(UiText::StringValue),
        )
    }
    if (titleLines.isEmpty() && aliases.isEmpty()) return null
    return AlsoKnownAsUIModel(
        titles = titleLines.toImmutableList(),
        aliases = aliases.map(UiText::StringValue).toImmutableList(),
    )
}
