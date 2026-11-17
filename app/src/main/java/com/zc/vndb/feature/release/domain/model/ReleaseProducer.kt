package com.zc.vndb.feature.release.domain.model

data class ReleaseProducer(
    val id: String,
    val name: String,
    val isDeveloper: Boolean,
    val isPublisher: Boolean,
)
