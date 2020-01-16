package com.aallam.underwave.internal.cache

import android.content.Context
import com.aallam.underwave.internal.cache.disk.DiskCache
import com.aallam.underwave.internal.cache.disk.DiskDataCache
import com.aallam.underwave.internal.cache.memory.MemoryCache
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapPool
import com.aallam.underwave.internal.extension.bytesToKilobytes
import com.aallam.underwave.internal.image.Bitmap

/**
 * Image cache repository.
 *
 * @param memoryCache in-memory cache
 * @param diskCache filesystem cache
 */
internal actual class ImageDataCache actual constructor(
    val memoryCache: MemoryCache,
    val diskCache: DiskCache
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

    companion object {
        private const val DISK_CACHE_SIZE = 100 * 1024L * 100L // 100mb
        private val MEMORY_CACHE_SIZE: Int = (Runtime.getRuntime().maxMemory() / 8).bytesToKilobytes

        /**
         * Creates a new [ImageDataCache] object.
         */
        @JvmStatic
        fun newInstance(context: Context, bitmapPool: BitmapPool): ImageDataCache {
            val memoryCache: MemoryCache = MemoryCache.newInstance(bitmapPool, MEMORY_CACHE_SIZE)
            val diskCache: DiskDataCache = DiskDataCache.newInstance(context, DISK_CACHE_SIZE)
            return ImageDataCache(
                memoryCache = memoryCache,
                diskCache = diskCache
            )
        }
    }
}
