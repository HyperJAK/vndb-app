package com.zc.vndb.feature.release.domain.model

import com.zc.vndb.common.domain.model.Cover

data class ReleaseImage(val type: ReleaseImageType, val image: Cover)
