package com.aallam.underwave.internal.cache.impl

import android.content.Context
import com.aallam.underwave.image.Bitmap
import com.aallam.underwave.internal.cache.ImageCache
import com.aallam.underwave.internal.cache.disk.DiskDataCache
import com.aallam.underwave.internal.cache.memory.MemoryCache

/**
 * Image cache repository.
 */
internal actual class ImageDataCache actual constructor(
    private val memoryCache: MemoryCache,
    private val diskCache: DiskDataCache
) : ImageCache {

    override fun put(url: String, bitmap: Bitmap) {
        diskCache.put(url, bitmap)
        memoryCache.put(url, bitmap)
    }

    @Synchronized
    override operator fun get(imageUrl: String): Bitmap? {
        return memoryCache[imageUrl] ?: diskCache[imageUrl]?.let { bitmap ->
            memoryCache.put(imageUrl, bitmap)
            return@let bitmap
        }
    }

    override fun clear() {
        memoryCache.clear()
        diskCache.clear()
    }

    companion object {
        private const val DEFAULT_DISK_CACHE_SIZE = 1024L * 1024L * 100L // 100mb
        private val DEFAULT_MEMORY_CACHE_SIZE = Runtime.getRuntime().maxMemory() / 8 // eighth

        /**
         * Creates a new [ImageDataCache] object.
         */
        @JvmStatic
        fun newInstance(
            context: Context,
            memoryCacheSize: Long = DEFAULT_MEMORY_CACHE_SIZE,
            diskCacheSize: Long = DEFAULT_DISK_CACHE_SIZE
        ): ImageDataCache {
            val memoryCache: MemoryCache = MemoryCache.newInstance(memoryCacheSize)
            val diskCache: DiskDataCache = DiskDataCache.newInstance(context, diskCacheSize)
            return ImageDataCache(
                memoryCache = memoryCache,
                diskCache = diskCache
            )
        }
    }
}
