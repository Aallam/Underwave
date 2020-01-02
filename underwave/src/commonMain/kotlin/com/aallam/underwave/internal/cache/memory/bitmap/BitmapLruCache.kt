package com.aallam.underwave.internal.cache.memory.bitmap

/**
 * Bitmap cache that holds strong references to a limited number of values. Each time
 * a value is accessed, it is moved to the head of a queue. When a value is
 * added to a full cache, the value at the end of that queue is evicted and may
 * become eligible for garbage collection.
 *
 * @param size cache size
 * @param bitmapPool pool of reusable bitmaps
 */
internal expect class BitmapLruCache(
    size: Int,
    bitmapPool: BitmapPool
)
