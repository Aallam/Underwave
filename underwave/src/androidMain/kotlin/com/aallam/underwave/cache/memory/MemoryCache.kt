package com.aallam.underwave.cache.memory

import android.util.LruCache
import com.aallam.underwave.image.Bitmap
import com.aallam.underwave.cache.Cache

internal actual class MemoryCache actual constructor(
    size: Long
) : Cache<String, Bitmap> {

    private val lruCache: LruCache<String, Bitmap> = newLruCache(size)

    private fun newLruCache(maxCacheSize: Long): LruCache<String, Bitmap> {
        val sizeInKilobytes: Int = (maxCacheSize / 1024).toInt()
        return object : LruCache<String, Bitmap>(sizeInKilobytes) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // the cache size will be measured in kilobytes rather than number of items.
                return bitmap.byteCount / 1024
            }
        }
    }

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
}