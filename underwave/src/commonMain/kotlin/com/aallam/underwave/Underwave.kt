package com.aallam.underwave

import com.aallam.underwave.image.ImageView

expect class Underwave {

    companion object {
        val PLATFORM: String
    }

    /**
     * Load the given image to an [ImageView].
     *
     * @param imageUrl image url to be loaded.
     * @param imageView image view to load the image into.
     */
    fun load(imageUrl: String, imageView: ImageView)
}