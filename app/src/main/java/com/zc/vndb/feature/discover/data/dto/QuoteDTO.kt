package com.zc.vndb.feature.discover.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A quote record from `POST /quote`. */
@Serializable
data class QuoteDTO(
    @SerialName("id") val id: String,
    @SerialName("quote") val quote: String,
    @SerialName("vn") val vn: QuoteNovelDTO,
    /** Null when VNDB does not attribute the quote to a character. */
    @SerialName("character") val character: QuoteCharacterDTO?,
)

@Serializable
data class QuoteNovelDTO(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("alttitle") val altTitle: String?,
)

@Serializable
data class QuoteCharacterDTO(@SerialName("name") val name: String?)
