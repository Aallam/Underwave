package com.aallam.underwave.internal.image

/**
 * A view to display [Bitmap] image resource.
 */
expect class ImageView {

    /**
     * Get view's width.
     */
    fun getWidth(): Int

    /**
     * Get height's height.
     */
    fun getHeight(): Int
}
