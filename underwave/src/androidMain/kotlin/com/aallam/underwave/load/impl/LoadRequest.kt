package com.aallam.underwave.load.impl

import com.aallam.underwave.internal.extension.log
import com.aallam.underwave.internal.image.ImageView
import com.aallam.underwave.load.Request
import java.lang.ref.WeakReference
import java.util.concurrent.Future

/**
 * A [Request] Implementation.
 */
internal actual class LoadRequest(
    internal val imageUrl: String,
    internal val imageView: WeakReference<ImageView>
) : Request {

    internal var request: Future<*>? = null

    override fun cancel() {
        request?.let {
            if (!it.isDone) {
                log("cancel request: $imageUrl")
                it.cancel(false)
            }
        }
    }

    companion object {

        /**
         * Creates a new [LoadRequest] object.
         */
        @JvmStatic
        fun newInstance(imageUrl: String, imageView: ImageView): LoadRequest {
            return LoadRequest(
                imageUrl = imageUrl,
                imageView = WeakReference(imageView)
            )
        }
    }
}
