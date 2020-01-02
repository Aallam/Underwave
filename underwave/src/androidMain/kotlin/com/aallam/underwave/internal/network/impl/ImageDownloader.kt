package com.aallam.underwave.internal.network.impl

import android.graphics.BitmapFactory
import com.aallam.underwave.internal.BitmapLoader
import com.aallam.underwave.internal.async.dispatcher.impl.SourceExecutor
import com.aallam.underwave.internal.cache.ImageCache
import com.aallam.underwave.internal.extension.log
import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.internal.image.Dimension
import com.aallam.underwave.internal.network.Downloader
import com.aallam.underwave.internal.view.ViewManager
import com.aallam.underwave.load.impl.LoadRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * Utility class to download an image using its URL.
 */
internal actual class ImageDownloader(
    private val imageCache: ImageCache,
    private val viewManager: ViewManager,
    private val bitmapLoader: BitmapLoader,
    private val dispatcher: CoroutineDispatcher
) : Downloader {

    /**
     * Download an image using its url and scale it to the given width and height.
     *
     * @param loadRequest load request to be applied.
     * @return cancellable load request
     */
    override suspend fun download(loadRequest: LoadRequest, display: Dimension) = coroutineScope {
        if (viewManager.isViewReused(loadRequest)) return@coroutineScope
        val url: String = loadRequest.imageUrl
        httpGet(url) { rawBitmap ->
            bitmapLoader.scale(rawBitmap, display, imageCache.bitmapPool) { bitmap ->
                imageCache.put(url, bitmap)
                viewManager.load(loadRequest, bitmap)
            }
        }
    }

    private suspend fun httpGet(url: String, onResult: suspend (Bitmap) -> Unit): Unit =
        coroutineScope<Unit> {
            launch(dispatcher) {
                var urlConnection: HttpURLConnection? = null
                try {
                    urlConnection = URL(url).openConnection() as HttpURLConnection
                    val inputStream: BufferedInputStream = urlConnection.inputStream.buffered()
                    val raw = BitmapFactory.decodeStream(inputStream)
                    onResult(raw)
                } catch (ex: IOException) {
                    log("Error while downloading $this", ex)
                    null
                } finally {
                    urlConnection?.disconnect()
                }
            }
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
            dispatcher: CoroutineDispatcher = SourceExecutor.dispatcher
        ): ImageDownloader {
            return ImageDownloader(
                imageCache = imageCache,
                viewManager = viewManager,
                bitmapLoader = bitmapLoader,
                dispatcher = dispatcher
            )
        }
    }
}
