package com.aallam.underwave.internal.view

import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.internal.image.ImageView
import com.aallam.underwave.load.impl.LoadRequest

/**
 * View operations handling.
 */
internal interface ViewManager {

    /**
     * Views mapped to URLs.
     */
    val viewMap: MutableMap<ImageView, String>

    /**
     * Post to the handler the bitmap loading into the image view operation.
     */
    suspend fun load(loadRequest: LoadRequest, bitmap: Bitmap)

    /**
     * Check if an image has already has been loaded to the [ImageView] or reused.
     */
    fun isViewReused(loadRequest: LoadRequest): Boolean
}
