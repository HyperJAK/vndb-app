package com.zc.vndb.feature.browse.domain.model

/** The genre shortcuts offered as chips, each backed by a VNDB tag id. */
enum class Genre(val tagId: String) {
    Romance("g96"),
    Mystery("g19"),
    SciFi("g105"),
    Horror("g7"),
    Psychological("g253"),
    Fantasy("g2"),
    Drama("g147"),
    Comedy("g104"),
    Action("g12"),
}
