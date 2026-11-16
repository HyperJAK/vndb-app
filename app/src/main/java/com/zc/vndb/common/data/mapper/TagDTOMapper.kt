package com.zc.vndb.common.data.mapper

import com.zc.vndb.common.data.dto.TagDTO
import com.zc.vndb.common.domain.model.NovelTag
import com.zc.vndb.common.domain.model.TagCategory

/** Unknown spoiler levels are treated as major spoilers so they stay hidden. */
private const val MAJOR_SPOILER = 2

/** Returns null for tags in a category the app does not know. */
fun TagDTO.toDomain(): NovelTag? {
    val category = category.toTagCategory() ?: return null
    return NovelTag(
        name = name,
        rating = rating ?: 0.0,
        spoilerLevel = spoiler ?: MAJOR_SPOILER,
        category = category,
    )
}

private fun String?.toTagCategory(): TagCategory? = when (this) {
    "cont" -> TagCategory.Content
    "ero" -> TagCategory.Sexual
    "tech" -> TagCategory.Technical
    else -> null
}
