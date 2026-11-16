package com.zc.vndb.feature.detail.domain.model

/** VNDB relation codes; anything new maps to [Other]. */
enum class RelationType {
    Sequel,
    Prequel,
    SameSetting,
    AlternativeVersion,
    SharesCharacters,
    SideStory,
    ParentStory,
    SameSeries,
    FanDisc,
    OriginalGame,
    Other,
}
