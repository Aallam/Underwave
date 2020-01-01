package com.aallam.underwave.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.aallam.underwave.storage.disk.CompressFormat
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

actual typealias Bitmap = Bitmap

/**
 * Scale the bitmap to the given dimensions and compress format.
 */
internal fun Bitmap.scale(width: Int, height: Int, format: CompressFormat = CompressFormat.JPEG): Bitmap? {
    if (width == 0 || height == 0) return this
    val stream = ByteArrayOutputStream()
    compress(format, 100, stream)
    val inputStream: BufferedInputStream = stream.toByteArray().inputStream().buffered()
    return inputStream.scale(width, height)
}

/**
 * Scale a bitmap to the given width and height.
 */
internal fun InputStream.scale(width: Int, height: Int): Bitmap? {
    return BitmapFactory.Options().run {
        mark(available()) // mark to reset later
        inJustDecodeBounds = true
        BitmapFactory.decodeStream(this@scale, null, this) // get original bitmap
        inSampleSize = calculateInSampleSize(width, height)
        inJustDecodeBounds = false
        reset()
        BitmapFactory.decodeStream(this@scale, null, this) // decode bitmap with inSampleSize set
    }
}

/**
 * The sample size is the number of pixels in either dimension that correspond to a single pixel in the decoded bitmap.
 * For example, inSampleSize == 4 returns an image that is 1/4 the width/height of the original.
 * This function calculates the sample size value corresponding the given width and height.
 */
private fun BitmapFactory.Options.calculateInSampleSize(width: Int, height: Int): Int {
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
