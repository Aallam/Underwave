package com.aallam.underwave.load

import com.aallam.underwave.extension.log
import com.aallam.underwave.image.ImageView
import java.lang.ref.WeakReference
import java.util.concurrent.Future

/**
 * A [LoadRequest] Implementation.
 */
class LoadDataRequest(
    internal val imageUrl: String,
    internal val imageView: WeakReference<ImageView>
) : LoadRequest {

    var request: Future<*>? = null

    override fun cancel() {
        request?.let {
            log("cancel request: $imageUrl")
            it.cancel(false)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(imageUrl: String, imageView: ImageView): LoadDataRequest {
            return LoadDataRequest(
                imageUrl,
                WeakReference(imageView)
            )
        }
    }
}