package com.aallam.underwave

import com.aallam.underwave.image.ImageView
import com.aallam.underwave.load.LoadRequest

/**
 * A singleton to present a simple static interface for building requests.
 */
expect class Underwave {

    /**
     * Load the given image to an [ImageView].
     *
     * @param imageUrl image url to be loaded.
     * @param imageView image view to load the image into.
     */
    fun load(imageUrl: String, imageView: ImageView): LoadRequest
}