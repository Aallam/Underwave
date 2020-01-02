package com.aallam.underwave.internal.view.impl

import android.content.Context
import android.os.Handler
import com.aallam.underwave.internal.BitmapLoader
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapPool
import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.internal.image.Dimension
import com.aallam.underwave.internal.image.ImageView
import com.aallam.underwave.internal.image.dimension
import com.aallam.underwave.internal.view.ViewManager
import com.aallam.underwave.load.impl.LoadRequest
import java.util.Collections.synchronizedMap
import java.util.WeakHashMap

internal actual class ImageViewManager(
    override val display: Dimension,
    override val viewMap: MutableMap<ImageView, String>,
    private val handler: Handler,
    private val bitmapPool: BitmapPool,
    private val bitmapLoader: BitmapLoader
) : ViewManager {

    override fun load(loadRequest: LoadRequest, bitmap: Bitmap) {
        if (isViewReused(loadRequest)) return
        loadRequest.imageView.get()?.let { view ->
            bitmapLoader.scale(bitmap, view.dimension, bitmapPool, loadRequest) { scaledBitmap ->
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
        fun newInstance(
            context: Context,
            bitmapPool: BitmapPool,
            bitmapLoader: BitmapLoader
        ): ImageViewManager {
            return ImageViewManager(
                display = context.resources.displayMetrics.dimension,
                viewMap = synchronizedMap(WeakHashMap()),
                handler = Handler(),
                bitmapPool = bitmapPool,
                bitmapLoader = bitmapLoader
            )
        }
    }
}
