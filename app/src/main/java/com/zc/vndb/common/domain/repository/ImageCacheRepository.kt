package com.zc.vndb.common.domain.repository

/** The on-disk image cache. */
interface ImageCacheRepository {

    /** Bytes currently used. */
    suspend fun size(): Long

    /** Empties the disk and memory caches. */
    suspend fun clear()
}
