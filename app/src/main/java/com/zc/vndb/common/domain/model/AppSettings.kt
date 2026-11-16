package com.zc.vndb.common.domain.model

/** The user's preferences, from the Settings screen. Defaults apply until something is changed. */
data class AppSettings(
    val titleLanguage: TitleLanguage = TitleLanguage.Romanized,
    /** Spoiler level VN and character pages start with. */
    val defaultSpoilerLevel: SpoilerPreference = SpoilerPreference.None,
    /** VNs on the signed-in user's Finished list open with every spoiler shown. */
    val spoilIfFinished: Boolean = false,
    /** Covers are not blurred, screenshots start with every content level, sexual tags are listed. */
    val showSensitiveContent: Boolean = false,
    /** Browse starts without an age filter instead of All Ages. */
    val includeAdultInBrowse: Boolean = false,
    /** VNDB language codes for Discover's New & Trending; never empty. */
    val trendingLanguages: List<String> = DEFAULT_TRENDING_LANGUAGES,
) {
    companion object {
        val DEFAULT_TRENDING_LANGUAGES = listOf("en", "ja")

        /** Release languages offered for New & Trending, in display order. */
        val TRENDING_LANGUAGE_CHOICES = listOf(
            "en", "ja", "zh-Hans", "zh-Hant", "ko", "es", "de", "fr", "ru", "pt-br", "it", "pl", "vi", "id", "th",
        )
    }
}

/** Which of a VN's titles comes first. */
enum class TitleLanguage {
    /** VNDB's main title, usually romanized (`title`). */
    Romanized,

    /** The title in its original script (`alttitle`), when there is one. */
    Original,
}

/** A spoiler level stored as a preference (the UI's spoiler filter maps it to its own type). */
enum class SpoilerPreference {
    None,
    Minor,
    Major,
}
