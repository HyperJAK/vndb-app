package com.zc.vndb.feature.detail.domain.model

import com.zc.vndb.common.domain.model.CharacterRole
import com.zc.vndb.common.domain.model.Cover

data class NovelCharacter(
    val id: String,
    val name: String,
    val originalName: String?,
    val image: Cover?,
    val role: CharacterRole,
    /** 0 = no spoiler, 1 = minor, 2 = major. */
    val spoilerLevel: Int,
    val voiceActor: String?,
)
