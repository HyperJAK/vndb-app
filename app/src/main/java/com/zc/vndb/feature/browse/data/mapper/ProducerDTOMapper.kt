package com.zc.vndb.feature.browse.data.mapper

import com.zc.vndb.feature.browse.data.dto.ProducerDTO
import com.zc.vndb.feature.browse.domain.model.ProducerInfo
import com.zc.vndb.feature.browse.domain.model.ProducerType

fun ProducerDTO.toDomain(): ProducerInfo = ProducerInfo(
    id = id,
    name = name,
    originalName = original?.takeIf { it.isNotBlank() && it != name },
    aliases = aliases.orEmpty().filter { it.isNotBlank() },
    type = type.toProducerType(),
    language = lang?.takeIf { it.isNotBlank() },
    description = description?.takeIf { it.isNotBlank() },
)

private fun String?.toProducerType(): ProducerType? = when (this) {
    "co" -> ProducerType.Company
    "in" -> ProducerType.Individual
    "ng" -> ProducerType.AmateurGroup
    else -> null
}
