package com.zc.vndb.feature.discover.data.mapper

import com.zc.vndb.feature.discover.data.dto.QuoteDTO
import com.zc.vndb.feature.discover.domain.model.QuoteOfTheDay

fun QuoteDTO.toDomain(): QuoteOfTheDay = QuoteOfTheDay(
    id = id,
    text = quote.trim(),
    novelId = vn.id,
    novelTitle = vn.title,
    novelOriginalTitle = vn.altTitle?.takeIf { it.isNotBlank() && it != vn.title },
    characterName = character?.name?.takeIf { it.isNotBlank() },
)
