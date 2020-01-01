package com.aallam.underwave

import android.content.Context
import android.os.Handler
import com.aallam.underwave.cache.ImageCache
import com.aallam.underwave.image.Bitmap
import com.aallam.underwave.image.Dimension
import com.aallam.underwave.image.ImageView
import com.aallam.underwave.image.dimension
import com.aallam.underwave.image.scale
import com.aallam.underwave.load.LoadDataRequest
import com.aallam.underwave.load.LoadRequest
import com.aallam.underwave.network.Downloader
import java.util.Collections.synchronizedMap
import java.util.WeakHashMap

actual class Underwave private constructor(context: Context) {
    private val display: Dimension = context.resources.displayMetrics.dimension
    private val viewMap: MutableMap<ImageView, String> = synchronizedMap(WeakHashMap())
    private val handler: Handler = Handler()
    private val imageCache: ImageCache = ImageCache(context)
    private val downloader: Downloader = Downloader(imageCache, ::isViewReused, ::handlerPost)

    actual fun load(imageUrl: String, imageView: ImageView): LoadRequest {
        require(imageUrl.isNotEmpty()) { "Underwave:load - Image Url should not be empty" }
        imageView.setImageResource(0)
        viewMap[imageView] = imageUrl

        val loadRequest: LoadDataRequest = LoadDataRequest.newInstance(imageUrl, imageView)

        // load from the cache.
        imageCache[imageUrl]?.let { bitmap ->
            loadBitmapIntoImageView(loadRequest, bitmap)
            return loadRequest
        }

        // otherwise, download.
        return downloader.download(loadRequest, display)
    }

    /**
     * Load a given [Bitmap] to the requested [ImageView].
     */
    @Synchronized
    private fun loadBitmapIntoImageView(loadRequest: LoadDataRequest, bitmap: Bitmap) {
        loadRequest.imageView.get()?.let { view ->
            val scaledBitmap: Bitmap = bitmap.scale(view.dimension) ?: return
            if (!isViewReused(loadRequest)) view.setImageBitmap(scaledBitmap)
        }
    }

    /**
     * Post to the handler the bitmap loading into the image view operation.
     */
    private fun handlerPost(loadRequest: LoadDataRequest, bitmap: Bitmap) {
        if (isViewReused(loadRequest)) return
        handler.post {
            if (isViewReused(loadRequest)) return@post
            loadBitmapIntoImageView(loadRequest, bitmap)
        }
    }

    /**
     * Check if an image has already has been loaded to the [ImageView] or reused.
     */
    private fun isViewReused(loadRequest: LoadDataRequest): Boolean {
        val view: ImageView? = loadRequest.imageView.get()
        val imageUrl: String? = viewMap[view]
        return imageUrl == null || imageUrl != loadRequest.imageUrl
    }

    /**
     * Clears as much memory and as possible and disk cache.
     */
    fun clear() {
        viewMap.clear()
        imageCache.clear()
    }

    /**
     * Clears all caches and stops all operations.
     */
    @Synchronized
    fun shutdown() {
        INSTANCE = null
        downloader.shutdown()
        clear()
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
                INSTANCE ?: Underwave(context).also { INSTANCE = it }
            }
        }
    }
}
