package com.zc.vndb.feature.character.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterNovelVoicesPageDTO(
    @SerialName("results") val results: List<CharacterNovelVoicesDTO>,
)
