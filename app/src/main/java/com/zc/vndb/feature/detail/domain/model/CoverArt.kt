package com.zc.vndb.feature.detail.domain.model

import com.zc.vndb.common.domain.model.Cover

/** A cover from one of the VN's releases. */
data class CoverArt(
    val image: Cover,
    val releaseId: String,
    val releaseTitle: String,
    val isPackageFront: Boolean,
)
