package com.zc.vndb.feature.detail.domain.model

/** One page of a paged list. */
data class ResultPage<T>(val items: List<T>, val hasMore: Boolean)
