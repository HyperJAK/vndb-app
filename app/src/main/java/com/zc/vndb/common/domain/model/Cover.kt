package com.zc.vndb.common.domain.model

/** A cover image with VNDB's averaged content flags (0 = safe, 1 = suggestive/violent, 2 = explicit/brutal). */
data class Cover(
    val url: String,
    val thumbnailUrl: String?,
    val sexual: Double,
    val violence: Double,
) {
    /** Covers at or above "suggestive" / "violent" are blurred until the user opts in. */
    val isSensitive: Boolean
        get() = sexual >= SENSITIVE_THRESHOLD || violence >= SENSITIVE_THRESHOLD

    private companion object {
        const val SENSITIVE_THRESHOLD = 1.0
    }
}
