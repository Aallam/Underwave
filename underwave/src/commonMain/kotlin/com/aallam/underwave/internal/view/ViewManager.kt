package com.aallam.underwave.internal.view

import com.aallam.underwave.image.Bitmap
import com.aallam.underwave.image.Dimension
import com.aallam.underwave.image.ImageView
import com.aallam.underwave.load.impl.LoadRequest

/**
 * View operations handling.
 */
internal interface ViewManager {

    /**
     * Current device's screen dimensions.
     */
    val display: Dimension

    /**
     * Views mapped to URLs.
     */
    val viewMap: MutableMap<ImageView, String>

    /**
     * Post to the handler the bitmap loading into the image view operation.
     */
    fun postHandler(loadRequest: LoadRequest, bitmap: Bitmap)

    /**
     * Check if an image has already has been loaded to the [ImageView] or reused.
     */
    fun isViewReused(loadRequest: LoadRequest): Boolean

    /**
     * Load a given [Bitmap] to the requested [ImageView].
     */
    fun loadBitmapIntoImageView(loadRequest: LoadRequest, bitmap: Bitmap)
}
