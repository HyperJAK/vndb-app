package com.zc.vndb.common.ui.component.spoilerfilter.model

/** How much spoiler content the reader allows; matches VNDB's 0 / 1 / 2 spoiler levels. */
enum class SpoilerLevel(val maxLevel: Int) {
    None(0),
    Minor(1),
    Major(2),
}
