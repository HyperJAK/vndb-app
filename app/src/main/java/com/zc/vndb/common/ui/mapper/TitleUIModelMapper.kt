package com.zc.vndb.common.ui.mapper

import com.zc.vndb.common.domain.model.TitleLanguage
import com.zc.vndb.common.ui.text.UiText

/*
 * A VN's titles in the order picked in Settings: [title] is VNDB's main title (usually romanized),
 * [original] the original-script one. Without a distinct original title, [title] is shown alone.
 */

/** The title shown first. */
fun primaryTitle(title: String, original: String?, language: TitleLanguage): UiText {
    val alternative = original.distinctFrom(title)
    return UiText.StringValue(
        if (language == TitleLanguage.Original && alternative != null) alternative else title,
    )
}

/** The title shown under [primaryTitle], or null when there is only one. */
fun secondaryTitle(title: String, original: String?, language: TitleLanguage): UiText? {
    val alternative = original.distinctFrom(title) ?: return null
    return UiText.StringValue(if (language == TitleLanguage.Original) title else alternative)
}

private fun String?.distinctFrom(title: String): String? = this?.takeIf {
    it.isNotBlank() &&
        it != title
}
