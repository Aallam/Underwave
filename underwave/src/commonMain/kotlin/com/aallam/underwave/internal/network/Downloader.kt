package com.aallam.underwave.internal.network

import com.aallam.underwave.internal.image.Bitmap

/**
 * Utility class to download an image using its URL.
 */
internal interface Downloader {

    /**
     * Download an image using its url and scale it to the given width and height.
     *
     * @param url image url to download.
     * @return cancellable load request.
     */
    suspend fun download(url: String): Bitmap?
}
