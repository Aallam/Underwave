package com.aallam.underwave.internal.cache

import com.aallam.underwave.internal.cache.disk.DiskCache
import com.aallam.underwave.internal.cache.memory.MemoryCache
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapPool
import com.aallam.underwave.internal.image.Bitmap

/**
 * Image cache repository.
 *
 * @param memoryCache in-memory cache
 * @param diskCache filesystem cache
 */
internal class ImageDataCache(
    private val memoryCache: MemoryCache,
    private val diskCache: DiskCache
) : ImageCache {

    override val bitmapPool: BitmapPool
        get() = memoryCache.bitmapPool

    override suspend fun put(url: String, bitmap: Bitmap) {
        diskCache.put(url, bitmap)
        memoryCache.put(url, bitmap)
    }

    override suspend fun get(imageUrl: String): Bitmap? {
        return memoryCache.get(imageUrl) ?: diskCache.get(imageUrl)?.let { bitmap ->
            memoryCache.put(imageUrl, bitmap)
            return@let bitmap
        }
    }

    override suspend fun clear() {
        memoryCache.clear()
        diskCache.clear()
    }
}
