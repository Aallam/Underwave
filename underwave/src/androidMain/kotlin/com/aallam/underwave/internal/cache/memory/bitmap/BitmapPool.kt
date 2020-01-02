package com.aallam.underwave.internal.cache.memory.bitmap

import android.graphics.BitmapFactory
import com.aallam.underwave.internal.image.Bitmap

/**
 * Bitmap pool to apply to use for bitmaps.
 */
internal actual interface BitmapPool {

    /**
     * add bitmap to the pool
     */
    fun put(bitmap: Bitmap)

    /**
     * Look for one to use for reusable bitmap and set it in [BitmapFactory.Options.inBitmap].
     */
    fun addInBitmapOptions(options: BitmapFactory.Options)

    /**
     * Bitmap pool size
     */
    val size: Int

    /**
     * Clear the bitmap pool.
     */
    fun clear()
}
