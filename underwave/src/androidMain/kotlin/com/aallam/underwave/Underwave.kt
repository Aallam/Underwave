package com.aallam.underwave

import android.content.Context
import com.aallam.underwave.internal.cache.ImageCache
import com.aallam.underwave.internal.extension.log
import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.internal.image.ImageView
import com.aallam.underwave.internal.network.Downloader
import com.aallam.underwave.internal.view.ViewManager
import com.aallam.underwave.load.Request
import com.aallam.underwave.load.impl.LoadRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * A singleton to present a simple static interface for building requests.
 */
actual class Underwave internal constructor(
    private val scope: CoroutineScope,
    private val imageCache: ImageCache,
    private val downloader: Downloader,
    private val viewManager: ViewManager
) {

    /**
     * Load [imageUrl] into [imageView].
     *
     * @param imageUrl image url to be loaded.
     * @param imageView image view to load the image into.
     * @return object representing the requested operation.
     */
    actual fun load(imageUrl: String, imageView: ImageView): Request {
        return prepare(imageUrl, imageView).also { request ->
            scope.launch(request.job) { startLoading(request) }
        }
    }

    /**
     * A suspendable method to load an image to an [ImageView] using the caller's coroutine scope.
     *
     * @param imageUrl image url to be loaded.
     * @param imageView image view to load the image into.
     * @return object representing the requested operation.
     */
    actual suspend fun insert(imageUrl: String, imageView: ImageView): Request = coroutineScope {
        prepare(imageUrl, imageView).also { request ->
            launch(request.job) { startLoading(request) }
        }
    }

    /**
     * Setup an image loading request.
     *
     * @param imageUrl image url to be loaded.
     * @param imageView image view to load the image into.
     * @return object representing the requested operation.
     */
    private fun prepare(imageUrl: String, imageView: ImageView): LoadRequest {
        require(imageUrl.isNotEmpty()) { "Underwave - Image Url should not be empty" }
        imageView.setImageResource(0)
        viewManager.viewMap[imageView] = imageUrl
        return LoadRequest.newInstance(imageUrl, imageView)
    }

    /**
     * Start image loading.
     *
     * @param request representing the requested operation.
     */
    private suspend fun startLoading(request: LoadRequest) {
        log("start request ${request.imageUrl}")
        getImageBitmap(request)?.let { bitmap -> viewManager.load(request, bitmap) }
    }

    /**
     * Get image bitmap from cache if exists, otherwise download it.
     *
     * @param request representing the requested operation.
     */
    private suspend fun getImageBitmap(request: LoadRequest): Bitmap? {
        val url: String = request.imageUrl
        return imageCache.get(url) ?: download(request)
    }

    /**
     * Download an image bitmap.
     *
     * @param request representing the requested operation.
     * @return bitmap corresponding the image, in case of a fail return null.
     */
    private suspend fun download(request: LoadRequest): Bitmap? {
        if (viewManager.isViewReused(request)) return null
        val url: String = request.imageUrl
        return downloader.download(url)?.also { bitmap -> imageCache.put(url, bitmap) }
    }

    /**
     * Clears as much memory and as possible and disk cache.
     */
    actual fun clear() {
        scope.launch {
            viewManager.viewMap.clear()
            imageCache.clear()
        }
    }

    companion object {
        @Volatile
        internal var INSTANCE: Underwave? = null
        @Volatile
        internal var DEBUG = false

        /**
         * Show log debug messages if true.
         */
        fun debug(debug: Boolean) {
            this.DEBUG = debug
        }

        /**
         * Begin a load with Underwave by passing in a context.
         *
         * @param context Any context, will not be retained.
         * @return Underwave instance.
         */
        fun with(context: Context): Underwave {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UnderwaveBuilder(context).build().also { INSTANCE = it }
            }
        }
    }
}
