package com.zc.vndb.common.ui.mapper

import com.zc.vndb.common.ui.text.UiText
import java.util.Locale

private const val LANGUAGES_LISTED = 2

/** VNDB language code → name in the device language ("zh-Hans" → "Chinese (Simplified)"). */
fun languageName(code: String): String =
    Locale.forLanguageTag(code).getDisplayName(Locale.getDefault()).ifBlank { code }
        .replaceFirstChar { it.titlecase(Locale.getDefault()) }

/** "English, Japanese", or "English, Japanese +2" for longer lists. */
fun List<String>.toLanguagesLabel(): UiText {
    val names = take(LANGUAGES_LISTED).joinToString(", ") { languageName(it) }
    val more = size - LANGUAGES_LISTED
    return UiText.StringValue(if (more > 0) "$names +$more" else names)
}
