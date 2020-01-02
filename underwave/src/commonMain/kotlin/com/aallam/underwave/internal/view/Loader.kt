package com.aallam.underwave.internal.view

import com.aallam.underwave.internal.cache.memory.bitmap.BitmapPool
import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.internal.image.CompressFormat
import com.aallam.underwave.internal.image.Dimension

/**
 * Loading and transformation operations.
 */
internal interface Loader {

    /**
     * Scale a given [bitmap] to a specific [dimension].
     *
     * @param bitmap bitmap to scale
     * @param dimension dimension to scape the bitmap to
     * @param bitmapPool pool to use for memory optimisations
     * @param format bitmap compress format
     * @return scaled bitmap to the view dimensions if everything gone well, otherwise null
     */
    suspend fun scale(
        bitmap: Bitmap,
        dimension: Dimension,
        bitmapPool: BitmapPool,
        format: CompressFormat = CompressFormat.JPEG
    ): Bitmap?
}
