package com.aallam.underwave.load.impl

import com.aallam.underwave.internal.extension.log
import com.aallam.underwave.internal.image.ImageView
import com.aallam.underwave.load.Request
import kotlinx.coroutines.Job

/**
 * A [Request] Implementation.
 */
internal class LoadRequest(
    internal val imageUrl: String,
    internal val imageView: WeakReference<ImageView>,
    internal val job: Job
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
        fun newInstance(imageUrl: String, imageView: ImageView): LoadRequest {
            return LoadRequest(
                imageUrl = imageUrl,
                imageView = WeakReference(imageView),
                job = Job()
            )
        }
    }
}
