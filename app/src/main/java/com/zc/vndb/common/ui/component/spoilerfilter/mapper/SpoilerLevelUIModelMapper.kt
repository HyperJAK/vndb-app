package com.zc.vndb.common.ui.component.spoilerfilter.mapper

import com.zc.vndb.common.domain.model.SpoilerPreference
import com.zc.vndb.common.ui.component.spoilerfilter.model.SpoilerLevel

/** The spoiler filter level for a stored preference. */
fun SpoilerPreference.toSpoilerLevel(): SpoilerLevel = when (this) {
    SpoilerPreference.None -> SpoilerLevel.None
    SpoilerPreference.Minor -> SpoilerLevel.Minor
    SpoilerPreference.Major -> SpoilerLevel.Major
}
