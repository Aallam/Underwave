package com.aallam.underwave.internal.view.impl

import android.os.Handler
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapPool
import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.internal.image.ImageView
import com.aallam.underwave.internal.image.dimension
import com.aallam.underwave.internal.view.Loader
import com.aallam.underwave.internal.view.ViewManager
import com.aallam.underwave.load.impl.LoadRequest
import kotlinx.coroutines.coroutineScope
import java.util.Collections.synchronizedMap
import java.util.WeakHashMap

/**
 * A [ViewManager] implementation.
 */
internal actual class ImageViewManager(
    override val viewMap: MutableMap<ImageView, String>,
    private val handler: Handler,
    private val bitmapPool: BitmapPool,
    private val loader: Loader
) : ViewManager {

    override suspend fun load(loadRequest: LoadRequest, bitmap: Bitmap): Unit = coroutineScope {
        if (isViewReused(loadRequest)) return@coroutineScope
        loadRequest.imageView.get()?.let { view ->
            loader.scale(bitmap, view.dimension, bitmapPool)?.let { scaledBitmap ->
                postHandler(loadRequest, scaledBitmap)
            }
        }
    }

    override fun isViewReused(loadRequest: LoadRequest): Boolean {
        val view: ImageView = loadRequest.imageView.get() ?: return false
        val imageUrl: String? = viewMap[view]
        return imageUrl == null || imageUrl != loadRequest.imageUrl
    }

    /**
     * Post a load request to the handler to be processed.
     */
    private fun postHandler(loadRequest: LoadRequest, bitmap: Bitmap) {
        handler.post {
            loadRequest.imageView.get()?.let { view ->
                if (!isViewReused(loadRequest)) view.setImageBitmap(bitmap)
            }
        }
    }

    companion object {

        /**
         * Create a new [ImageViewManager] object.
         */
        @JvmStatic
        fun newInstance(loader: Loader, pool: BitmapPool): ImageViewManager {
            return ImageViewManager(
                viewMap = synchronizedMap(WeakHashMap()),
                handler = Handler(),
                bitmapPool = pool,
                loader = loader
            )
        }
    }
}
