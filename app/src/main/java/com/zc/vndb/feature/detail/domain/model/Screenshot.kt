package com.zc.vndb.feature.detail.domain.model

import com.zc.vndb.common.domain.model.Cover

/** A screenshot with VNDB's content flags (0 safe / tame … 2 explicit / brutal). */
data class Screenshot(val image: Cover, val releaseId: String?, val releaseTitle: String?)
