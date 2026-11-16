package com.zc.vndb.feature.discover.domain.model

/** Staff roles from the VNDB schema; roles the app does not use map to [Other]. */
enum class StaffRole {
    Scenario,
    CharacterDesign,
    Art,
    Music,
    Director,
    Other,
}
