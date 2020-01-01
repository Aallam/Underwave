package com.aallam.underwave.cache

import android.content.Context
import com.aallam.underwave.cache.disk.DiskCache
import com.aallam.underwave.cache.disk.getDiskCacheDirectory
import com.aallam.underwave.cache.memory.MemoryCache
import com.aallam.underwave.image.Bitmap

internal actual class ImageCache(
    context: Context,
    memoryCacheSize: Long = DEFAULT_MEMORY_CACHE_SIZE,
    diskCacheSize: Long = DEFAULT_DISK_CACHE_SIZE
) {
    actual val memoryCache: MemoryCache = MemoryCache(memoryCacheSize)
    actual val diskCache: DiskCache = DiskCache(context.getDiskCacheDirectory(), diskCacheSize)

    actual fun put(url: String, bitmap: Bitmap) {
        diskCache.put(url, bitmap)
        memoryCache.put(url, bitmap)
    }

    @Synchronized
    actual operator fun get(imageUrl: String): Bitmap? {
        return memoryCache.get(imageUrl) ?: diskCache.get(imageUrl)?.let {
            memoryCache.put(imageUrl, it)
            return@let it
        }
    }

    /**
     * Clears as much memory and as possible and disk cache.
     */
    actual fun clear() {
        memoryCache.clear()
        diskCache.clear()
    }

    companion object {
        private const val DEFAULT_DISK_CACHE_SIZE = 1024L * 1024L * 100L // 100mb
        private val DEFAULT_MEMORY_CACHE_SIZE = Runtime.getRuntime().maxMemory() / 8 // eighth
    }
}