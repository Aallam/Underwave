package com.aallam.underwave.internal.view.impl

import android.graphics.BitmapFactory
import com.aallam.underwave.internal.async.UnderwaveDispatchers
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapPool
import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.internal.image.CompressFormat
import com.aallam.underwave.internal.image.Dimension
import com.aallam.underwave.internal.view.Loader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * An implementation of [Loader].
 */
internal actual class BitmapLoader actual constructor(
    private val dispatcher: CoroutineDispatcher
) : Loader {

    override suspend fun scale(
        bitmap: Bitmap,
        dimension: Dimension,
        bitmapPool: BitmapPool,
        format: CompressFormat
    ): Bitmap? = withContext(dispatcher) {
        bitmap.scale(dimension, bitmapPool, format)
    }

    /**
     * Scale the bitmap to the given dimensions and compress format.
     */
    private fun Bitmap.scale(
        dimension: Dimension,
        bitmapPool: BitmapPool,
        format: CompressFormat = CompressFormat.JPEG
    ): Bitmap? {
        if (dimension.isEmpty()) return this
        val stream = ByteArrayOutputStream()
        compress(format, 100, stream)
        val inputStream: BufferedInputStream = stream.toByteArray().inputStream().buffered()
        return inputStream.scale(dimension, bitmapPool)
    }

    /**
     * Scale a bitmap to the given width and height.
     */
    private fun InputStream.scale(dimension: Dimension, bitmapPool: BitmapPool): Bitmap? {
        return BitmapFactory.Options().run {
            mark(available()) // mark to reset later
            inJustDecodeBounds = true
            // get original bitmap
            BitmapFactory.decodeStream(this@scale, null, this)
            inSampleSize = calculateInSampleSize(dimension)
            bitmapPool.addInBitmapOptions(this)
            inJustDecodeBounds = false
            reset()
            // decode bitmap with inSampleSize set
            BitmapFactory.decodeStream(this@scale, null, this)
        }
    }

    /**
     * The sample size is the number of pixels in either dimension that correspond to a single pixel in the decoded bitmap.
     * For example, inSampleSize == 4 returns an image that is 1/4 the width/height of the original.
     * This function calculates the sample size value corresponding the given width and height.
     */
    private fun BitmapFactory.Options.calculateInSampleSize(dimension: Dimension): Int {
        val (width: Int, height: Int) = dimension
        if (outHeight > height || outWidth > width) {
            var inSampleSize = 1
            val halfHeight: Int = outHeight / 2
            val halfWidth: Int = outWidth / 2
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= height && halfWidth / inSampleSize >= width) inSampleSize *= 2
            return inSampleSize
        }
        return 1
    }

    companion object {

        /**
         * Create a new [BitmapLoader] object.
         */
        @JvmStatic
        fun newInstance(): BitmapLoader {
            return BitmapLoader(UnderwaveDispatchers.Default)
        }
    }
}
