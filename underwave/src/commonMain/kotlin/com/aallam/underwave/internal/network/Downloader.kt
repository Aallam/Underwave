package com.aallam.underwave.internal.network

import com.aallam.underwave.internal.image.Dimension
import com.aallam.underwave.load.impl.LoadRequest

/**
 * Utility class to download an image using its URL.
 */
internal interface Downloader {

    /**
     * Download an image using its url and scale it to the given width and height.
     *
     * @param loadRequest load request to be applied.
     * @return cancellable load request
     */
    suspend fun download(loadRequest: LoadRequest, display: Dimension)
}
