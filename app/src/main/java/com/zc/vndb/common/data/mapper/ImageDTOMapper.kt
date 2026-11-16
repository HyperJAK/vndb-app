package com.zc.vndb.common.data.mapper

import com.zc.vndb.common.data.dto.ImageDTO
import com.zc.vndb.common.domain.model.Cover

/** Missing content flags count as safe (0); an image without a URL is no cover. */
fun ImageDTO.toCover(): Cover? = url?.let {
    Cover(
        url = it,
        thumbnailUrl = thumbnail,
        sexual = sexual ?: 0.0,
        violence = violence ?: 0.0,
    )
}
