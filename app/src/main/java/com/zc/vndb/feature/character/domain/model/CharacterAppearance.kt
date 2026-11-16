package com.zc.vndb.feature.character.domain.model

import com.zc.vndb.common.domain.model.CharacterRole
import com.zc.vndb.common.domain.model.Cover
import com.zc.vndb.common.domain.model.ReleaseDate

/** The character's role in one VN. */
data class CharacterAppearance(
    val novelId: String,
    val novelTitle: String,
    val releaseDate: ReleaseDate,
    val rating: Double?,
    val cover: Cover?,
    val role: CharacterRole,
    val spoilerLevel: Int,
    val voiceActor: String?,
    /** The VN's title in the original script, when VNDB has one. */
    val novelOriginalTitle: String? = null,
)
