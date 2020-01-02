package com.aallam.underwave.internal.image

/**
 * A representation of a bitmap image.
 */
expect class Bitmap

/**
 * Bitmap Compress Format.
 */
expect enum class CompressFormat {
    JPEG, PNG, WEBP;
}

/**
 * Possible bitmap configurations.
 */
expect enum class Config {
    ALPHA_8, RGB_565, ARGB_4444, ARGB_8888, RGBA_F16, HARDWARE;
}
