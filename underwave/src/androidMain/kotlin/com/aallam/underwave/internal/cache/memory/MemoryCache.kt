package com.aallam.underwave.internal.cache.memory

import android.util.LruCache
import com.aallam.underwave.image.Bitmap
import com.aallam.underwave.internal.cache.Cache

/**
 * A memory [Cache] implantation that holds references to a limited number of values.
 *
 * @param size the maximum sum of the sizes of the entries in the cache in bytes.
 */
internal actual class MemoryCache(
    private val lruCache: LruCache<String, Bitmap>
) : Cache<String, Bitmap> {

    override fun get(key: String): Bitmap? {
        return lruCache[key]
    }

    override fun put(key: String, value: Bitmap) {
        lruCache.put(key, value)
    }

    override fun contains(key: String): Boolean {
        return lruCache.get(key) != null
    }

    override fun size(): Long {
        return lruCache.size() * 1024L // convert to bytes
    }

    override fun clear() {
        lruCache.evictAll()
    }

    companion object {

        /**
         * Creates a new [MemoryCache] object.
         */
        @JvmStatic
        fun newInstance(size: Long): MemoryCache {
            val lruCache: LruCache<String, Bitmap> = newLruCache(size)
            return MemoryCache(lruCache)
        }

        private fun newLruCache(maxCacheSize: Long): LruCache<String, Bitmap> {
            val sizeInKilobytes: Int = (maxCacheSize / 1024).toInt()
            return object : LruCache<String, Bitmap>(sizeInKilobytes) {
                override fun sizeOf(key: String, bitmap: Bitmap): Int {
                    // the cache size will be measured in kilobytes rather than number of items.
                    return bitmap.byteCount / 1024
                }
            }
        }
    }
}
