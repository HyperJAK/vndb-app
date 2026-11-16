package com.zc.vndb.feature.browse.data.mapper

import com.zc.vndb.common.data.mapper.toTagCategory
import com.zc.vndb.feature.browse.data.dto.TagInfoDTO
import com.zc.vndb.feature.browse.domain.model.TagInfo

fun TagInfoDTO.toDomain(): TagInfo = TagInfo(
    id = id,
    name = name,
    aliases = aliases.orEmpty().filter { it.isNotBlank() },
    description = description?.takeIf { it.isNotBlank() },
    category = category.toTagCategory(),
    novelCount = vnCount ?: 0,
)
