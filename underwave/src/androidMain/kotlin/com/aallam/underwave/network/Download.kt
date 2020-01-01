package com.aallam.underwave.network

import com.aallam.underwave.image.Bitmap
import com.aallam.underwave.image.scale
import java.io.BufferedInputStream
import java.net.URL

/**
 * Download an image using its url (as string) and scale it to the given width and height.
 */
internal fun String.download(width: Int, height: Int): Bitmap? {
    val inputStream: BufferedInputStream = URL(this).openConnection().getInputStream().buffered()
    return inputStream.scale(width, height)
}