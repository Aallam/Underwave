package com.aallam.underwave.internal.cache.memory.bitmap

import com.aallam.underwave.internal.image.Bitmap

/**
 * Bitmap pool to apply to use for bitmaps.
 */
internal expect interface BitmapPool {

    /**
     * add bitmap to the pool
     */
    fun put(bitmap: Bitmap)

    /**
     * Bitmap pool size
     */
    val size: Int

    /**
     * Clear the bitmap pool.
     */
    fun clear()
}
