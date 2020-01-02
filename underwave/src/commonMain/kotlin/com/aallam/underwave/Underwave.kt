package com.aallam.underwave

import com.aallam.underwave.internal.image.ImageView
import com.aallam.underwave.load.Request

/**
 * A singleton to present a simple static interface for building requests.
 */
expect class Underwave {

    /**
     * Load the given image to an [ImageView].
     *
     * @param imageUrl image url to be loaded.
     * @param imageView image view to load the image into.
     * @return object representing the requested operation.
     */
    fun load(imageUrl: String, imageView: ImageView): Request

    /**
     * Clears as much memory and as possible and disk cache.
     */
    fun clear()

    /**
     * Clears all caches and stops all operations.
     */
    fun shutdown()
}
