package com.aallam.underwave

import android.content.Context
import com.aallam.underwave.internal.async.UnderwaveScope
import com.aallam.underwave.internal.async.dispatcher.impl.LoadExecutor
import com.aallam.underwave.internal.cache.ImageCache
import com.aallam.underwave.internal.image.ImageView
import com.aallam.underwave.internal.network.Downloader
import com.aallam.underwave.internal.view.ViewManager
import com.aallam.underwave.load.Request
import com.aallam.underwave.load.impl.LoadRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * A singleton to present a simple static interface for building requests.
 */
actual class Underwave internal constructor(
    private val imageCache: ImageCache,
    private val downloader: Downloader,
    private val viewManager: ViewManager
) {

    private val scope: CoroutineScope = UnderwaveScope()

    /**
     * Load the given image to an [ImageView].
     *
     * @param imageUrl image url to be loaded.
     * @param imageView image view to load the image into.
     * @return object representing the requested operation.
     */
    actual fun load(imageUrl: String, imageView: ImageView): Request {
        require(imageUrl.isNotEmpty()) { "Underwave:load - Image Url should not be empty" }
        imageView.setImageResource(0)
        viewManager.viewMap[imageView] = imageUrl
        return LoadRequest.newInstance(imageUrl, imageView).also(::startLoading)
    }

    /**
     * Start image loading.
     */
    private fun startLoading(loadRequest: LoadRequest) {
        scope.launch(loadRequest.job) {
            imageCache.load(
                loadRequest,
                { viewManager.load(loadRequest, it) },
                { downloader.download(loadRequest, viewManager.display) })
        }
    }

    /**
     * Clears as much memory and as possible and disk cache.
     */
    actual fun clear() {
        viewManager.viewMap.clear()
        imageCache.clear()
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
