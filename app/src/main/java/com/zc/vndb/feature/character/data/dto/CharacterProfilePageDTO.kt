package com.zc.vndb.feature.character.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterProfilePageDTO(@SerialName("results") val results: List<CharacterProfileDTO>)
