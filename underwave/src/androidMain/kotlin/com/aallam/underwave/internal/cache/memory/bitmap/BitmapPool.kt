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
    actual fun put(bitmap: Bitmap)

    /**
     * Bitmap pool size
     */
    actual val size: Int

    /**
     * Clear the bitmap pool.
     */
    actual fun clear()

    /**
     * Look for one to use for reusable bitmap and set it in [BitmapFactory.Options.inBitmap].
     */
    fun addInBitmapOptions(options: BitmapFactory.Options)
}
