package com.aallam.underwave

/**
 * A representation of a bitmap image.
 */
expect class Bitmap {

    /**
     * The minimum number of bytes that can be used to store this bitmap's pixels.
     */
    fun getByteCount(): Int
}