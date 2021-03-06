package com.aallam.underwave.load.impl

import com.aallam.underwave.internal.extension.log
import com.aallam.underwave.internal.image.ImageView
import com.aallam.underwave.load.Request
import kotlinx.coroutines.Job
import java.lang.ref.WeakReference

/**
 * A [Request] Implementation.
 */
internal actual class LoadRequest(
    internal val imageUrl: String,
    internal val imageView: WeakReference<ImageView>,
    internal val job: Job = Job()
) : Request {

    override fun cancel() {
        if (job.isActive) { // if the job is active (running) cancel it
            log("cancel request: $imageUrl")
            job.cancel()
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
