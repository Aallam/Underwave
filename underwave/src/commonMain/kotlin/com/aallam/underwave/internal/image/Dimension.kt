package com.aallam.underwave.internal.image

/**
 * A class to encapsulate the width and height of a component.
 *
 * @param width the height dimension.
 * @param height the width dimension.
 */
internal expect class Dimension(width: Int, height: Int) {

    /**
     * Check if any of the dimension equals 0.
     *
     * @return true if width or height equals 0, otherwise false.
     */
    fun isEmpty(): Boolean
}
