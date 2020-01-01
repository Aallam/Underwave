package com.aallam.underwave.internal.view.impl

import android.content.Context
import android.os.Handler
import com.aallam.underwave.image.Bitmap
import com.aallam.underwave.image.Dimension
import com.aallam.underwave.image.ImageView
import com.aallam.underwave.image.dimension
import com.aallam.underwave.image.scale
import com.aallam.underwave.internal.view.ViewManager
import com.aallam.underwave.load.impl.LoadRequest
import java.util.Collections.synchronizedMap
import java.util.WeakHashMap

internal actual class ImageViewManager(
    override val display: Dimension,
    override val viewMap: MutableMap<ImageView, String>,
    private val handler: Handler
) : ViewManager {

    /**
     * Load a given [Bitmap] to the requested [ImageView].
     */
    @Synchronized
    override fun loadBitmapIntoImageView(loadRequest: LoadRequest, bitmap: Bitmap) {
        loadRequest.imageView.get()?.let { view ->
            val scaledBitmap: Bitmap = bitmap.scale(view.dimension) ?: return
            if (!isViewReused(loadRequest)) view.setImageBitmap(scaledBitmap)
        }
    }

    override fun postHandler(loadRequest: LoadRequest, bitmap: Bitmap) {
        if (isViewReused(loadRequest)) return
        handler.post {
            if (isViewReused(loadRequest)) return@post
            loadBitmapIntoImageView(loadRequest, bitmap)
        }
    }

    override fun isViewReused(loadRequest: LoadRequest): Boolean {
        val view: ImageView? = loadRequest.imageView.get()
        val imageUrl: String? = viewMap[view]
        return imageUrl == null || imageUrl != loadRequest.imageUrl
    }

    companion object {

        /**
         * Create a new [ImageViewManager] object.
         */
        @JvmStatic
        fun newInstance(context: Context): ImageViewManager {
            return ImageViewManager(
                display = context.resources.displayMetrics.dimension,
                viewMap = synchronizedMap(WeakHashMap()),
                handler = Handler()
            )
        }
    }
}
