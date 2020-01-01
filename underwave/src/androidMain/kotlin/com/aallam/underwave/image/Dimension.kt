package com.aallam.underwave.image

import android.util.DisplayMetrics

/**
 * A class to encapsulate the width and height of a component.
 *
 * @param width the height dimension.
 * @param height the width dimension.
 */
internal data class Dimension(val width: Int, val height: Int) {

    /**
     * Check if any of the dimension equals 0.
     *
     * @return true if width or height equals 0, otherwise false.
     */
    fun isEmpty(): Boolean {
        return width == 0 || height == 0
    }
}

internal val ImageView.dimension: Dimension
    get() = Dimension(width, height)

internal val DisplayMetrics.dimension: Dimension
    get() = Dimension(widthPixels, heightPixels)