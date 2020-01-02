package com.aallam.underwave.internal.cache.memory.bitmap

import android.util.LruCache
import com.aallam.underwave.internal.extension.bytesToKilobytes
import com.aallam.underwave.internal.image.Bitmap

/**
 * Bitmap cache that holds strong references to a limited number of values. Each time
 * a value is accessed, it is moved to the head of a queue. When a value is
 * added to a full cache, the value at the end of that queue is evicted and may
 * become eligible for garbage collection.
 */
internal actual class BitmapLruCache actual constructor(
    size: Int,
    private val bitmapPool: BitmapPool
) : LruCache<String, Bitmap>(size) {

    override fun sizeOf(key: String, value: Bitmap): Int {
        return value.allocationByteCount.bytesToKilobytes
    }

    override fun entryRemoved(
        evicted: Boolean,
        key: String?,
        oldValue: Bitmap?,
        newValue: Bitmap?
    ) {
        oldValue?.let { bitmap ->
            bitmapPool.put(bitmap)
        }
    }

    companion object {

        /**
         * Create a new [BitmapLruCache] object.
         */
        @JvmStatic
        fun newInstance(size: Int, bitmapPool: BitmapPool): BitmapLruCache {
            return BitmapLruCache(size, bitmapPool)
        }
    }
}
