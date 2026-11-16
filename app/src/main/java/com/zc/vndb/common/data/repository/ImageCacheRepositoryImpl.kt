package com.zc.vndb.common.data.repository

import android.content.Context
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import com.zc.vndb.common.domain.repository.ImageCacheRepository
import com.zc.vndb.common.domain.util.DispatcherProvider
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single

/** Coil's caches: the disk cache the image loader uses (see `CommonModule`) and its memory cache. */
@Single
class ImageCacheRepositoryImpl(
    private val context: Context,
    private val diskCache: DiskCache,
    private val dispatchers: DispatcherProvider,
) : ImageCacheRepository {

    override suspend fun size(): Long = withContext(dispatchers.io) { diskCache.size }

    override suspend fun clear() {
        withContext(dispatchers.io) { diskCache.clear() }
        SingletonImageLoader.get(context).memoryCache?.clear()
    }
}
