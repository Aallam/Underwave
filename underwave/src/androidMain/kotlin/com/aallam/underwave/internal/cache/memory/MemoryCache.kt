package com.aallam.underwave.internal.cache.memory

import com.aallam.underwave.internal.cache.Cache
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapLruCache
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapPool
import com.aallam.underwave.internal.extension.log
import com.aallam.underwave.internal.image.Bitmap

/**
 * A memory [Cache] implantation that holds references to a limited number of values.
 *
 * @param bitmapPool bitmap pool to hold references to bitmaps.
 * @param bitmapLruCache bitmap LRU cache.
 */
internal actual class MemoryCache actual constructor(
    val bitmapPool: BitmapPool,
    private val bitmapLruCache: BitmapLruCache
) : Cache<String, Bitmap> {

    override suspend fun get(key: String): Bitmap? {
        return bitmapLruCache.get(key)
    }

    override suspend fun put(key: String, value: Bitmap) {
        bitmapLruCache.put(key, value)
    }

    override fun size(): Long {
        return bitmapLruCache.size().toLong()
    }

    override suspend fun clear() {
        log("clear memory: ${size()} kb")
        bitmapLruCache.evictAll()
        log("clear bitmap pool: ${bitmapPool.size} elements")
        bitmapPool.clear()
    }

    companion object {

        /**
         * Creates a new [MemoryCache] object.
         */
        @JvmStatic
        fun newInstance(bitmapPool: BitmapPool, size: Int): MemoryCache {
            val bitmapLruCache = BitmapLruCache(size, bitmapPool)
            return MemoryCache(
                bitmapPool = bitmapPool,
                bitmapLruCache = bitmapLruCache
            )
        }
    }
}
