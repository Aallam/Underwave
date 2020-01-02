package com.aallam.underwave.internal.network.impl

import android.graphics.BitmapFactory
import com.aallam.underwave.internal.BitmapLoader
import com.aallam.underwave.internal.cache.ImageCache
import com.aallam.underwave.internal.executor.SourceExecutor
import com.aallam.underwave.internal.extension.log
import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.internal.image.Dimension
import com.aallam.underwave.internal.network.Downloader
import com.aallam.underwave.internal.view.ViewManager
import com.aallam.underwave.load.impl.LoadRequest
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService

/**
 * Utility class to download an image using its URL.
 */
internal actual class ImageDownloader(
    private val imageCache: ImageCache,
    private val viewManager: ViewManager,
    private val bitmapLoader: BitmapLoader,
    private val executorService: ExecutorService
) : Downloader {

    /**
     * Download an image using its url and scale it to the given width and height.
     *
     * @param loadRequest load request to be applied.
     * @return cancellable load request
     */
    override fun download(loadRequest: LoadRequest, display: Dimension) {
        loadRequest.request = executorService.submit {
            if (viewManager.isViewReused(loadRequest)) return@submit
            val url: String = loadRequest.imageUrl
            var urlConnection: HttpURLConnection? = null
            try {
                urlConnection = URL(url).openConnection() as HttpURLConnection
                val inputStream: BufferedInputStream = urlConnection.inputStream.buffered()
                val raw: Bitmap = BitmapFactory.decodeStream(inputStream)
                bitmapLoader.scale(raw, display, imageCache.bitmapPool, loadRequest) { bitmap ->
                    imageCache.put(url, bitmap)
                    viewManager.load(loadRequest, bitmap)
                }
            } catch (ex: IOException) {
                log("Error while downloading $this", ex)
                throw ex
            } finally {
                urlConnection?.disconnect()
            }
        }
    }

    /**
     * Initiates an orderly shutdown in which previously submitted
     * requests are executed, but no new tasks will be accepted.
     */
    override fun shutdown() {
        executorService.shutdown()
    }

    companion object {

        /**
         * Creates a new [ImageDownloader] object.
         */
        @JvmStatic
        fun newInstance(
            imageCache: ImageCache,
            viewManager: ViewManager,
            bitmapLoader: BitmapLoader,
            executorService: ExecutorService = SourceExecutor
        ): ImageDownloader {
            return ImageDownloader(
                imageCache = imageCache,
                viewManager = viewManager,
                bitmapLoader = bitmapLoader,
                executorService = executorService
            )
        }
    }
}
