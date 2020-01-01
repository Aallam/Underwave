package com.aallam.underwave.network

import com.aallam.underwave.cache.ImageCache
import com.aallam.underwave.extension.log
import com.aallam.underwave.image.Bitmap
import com.aallam.underwave.image.Dimension
import com.aallam.underwave.image.scale
import com.aallam.underwave.load.LoadDataRequest
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

/**
 * Utility class to download an image using its URL.
 */
internal class Downloader(
    private val imageCache: ImageCache,
    private val validate: (LoadDataRequest) -> Boolean,
    private val onDownloaded: (loadRequest: LoadDataRequest, bitmap: Bitmap) -> Unit
) {
    private val executorService: ExecutorService = UnderwaveExecutor()

    /**
     * Download an image using its url and scale it to the given width and height.
     *
     * @param loadRequest load request to be applied.
     * @return cancellable load request
     */
    internal fun download(loadRequest: LoadDataRequest, display: Dimension): LoadDataRequest {
        val request: Future<*> = executorService.submit {
            if (validate(loadRequest)) return@submit
            val url: String = loadRequest.imageUrl
            download(loadRequest.imageUrl, display)
                ?.also { bitmap -> imageCache.put(url, bitmap) }
                ?.let { bitmap -> onDownloaded(loadRequest, bitmap) }
        }
        return loadRequest.apply { this.request = request }
    }

    /**
     * Download an image using its url and scale it to the given width and height.
     *
     * @param url image url
     * @param dimension component dimensions
     * @return bitmap corresponding to the downloaded image.
     */
    private fun download(url: String, dimension: Dimension): Bitmap? {
        var urlConnection: HttpURLConnection? = null
        return try {
            urlConnection = URL(url).openConnection() as HttpURLConnection
            urlConnection.inputStream.buffered().use { inputStream ->
                inputStream.scale(dimension)
            }
        } catch (ex: IOException) {
            log("Error while downloading $this", ex)
            throw ex
        } finally {
            urlConnection?.disconnect()
        }
    }

    /**
     * Initiates an orderly shutdown in which previously submitted
     * requests are executed, but no new tasks will be accepted.
     */
    internal fun shutdown() {
        executorService.shutdown()
    }
}