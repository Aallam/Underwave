package com.aallam.underwave.internal.image

/**
 * A class to encapsulate the width and height of a component.
 *
 * @param width the height dimension.
 * @param height the width dimension.
 */
internal actual data class Dimension actual constructor(
    val width: Int,
    val height: Int
) {

    /**
     * Check if any of the dimension equals 0.
     *
     * @return true if width or height equals 0, otherwise false.
     */
    actual fun isEmpty(): Boolean {
        return width == 0 || height == 0
    }
}

internal val ImageView.dimension: Dimension
    get() = Dimension(width, height)
