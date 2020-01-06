package com.aallam.underwave.internal.network.impl

import com.aallam.underwave.internal.extension.log
import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.internal.network.Downloader
import com.aallam.underwave.internal.network.HttpClient
import kotlinx.coroutines.coroutineScope

/**
 * Utility class to download an image using its URL.
 */
internal actual class ImageDownloader(
    private val httpClient: HttpClient<Bitmap>
) : Downloader {

    /**
     * Download an image using its url and scale it to the given width and height.
     *
     * @param url image url to download.
     * @return image as [Bitmap] if success, otherwise null.
     */
    override suspend fun download(url: String): Bitmap? = coroutineScope {
        log("downloading $url")
        httpClient.get(url)
    }

    companion object {

        /**
         * Creates a new [ImageDownloader] object.
         */
        @JvmStatic
        fun newInstance(httpClient: HttpClient<Bitmap>): ImageDownloader {
            return ImageDownloader(httpClient)
        }
    }
}
