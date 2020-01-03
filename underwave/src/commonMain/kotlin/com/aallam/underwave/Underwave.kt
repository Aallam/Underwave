package com.aallam.underwave

import com.aallam.underwave.internal.DebugConfig
import com.aallam.underwave.internal.image.ImageView
import com.aallam.underwave.load.Request
import com.aallam.underwave.log.Logger

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
     * A suspendable method to load an image to an [ImageView] using the caller's coroutine scope.
     *
     * @param imageUrl image url to be loaded.
     * @param imageView image view to load the image into.
     * @return object representing the requested operation.
     */
    suspend fun insert(imageUrl: String, imageView: ImageView): Request

    /**
     * Clears as much memory and as possible and disk cache.
     */
    fun clear()

    companion object {

        internal var debug: DebugConfig

        /**
         * Enable or disable Underwave's debug mode.
         *
         * @param enabled enable the debug mode.
         * @param logger logging implementation.
         */
        fun debugMode(enabled: Boolean, logger: Logger = Logger.Default)
    }
}
