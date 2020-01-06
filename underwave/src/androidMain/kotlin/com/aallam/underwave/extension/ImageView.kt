package com.aallam.underwave.extension

import com.aallam.underwave.Underwave
import com.aallam.underwave.internal.image.ImageView
import com.aallam.underwave.load.Request
import kotlinx.coroutines.coroutineScope

/**
 * Load the given image url to this [ImageView].
 *
 * @param imageUrl image url to be loaded.
 */
fun ImageView.load(imageUrl: String): Request {
    return Underwave.with(this.context.applicationContext).load(imageUrl, this)
}

/**
 * A suspendable method to load an image to an [ImageView] using the caller's coroutine scope.
 *
 * @param imageUrl image url to be loaded.
 * @return object representing the requested operation.
 */
suspend fun ImageView.insert(imageUrl: String): Request = coroutineScope {
    Underwave.with(this@insert.context.applicationContext).insert(imageUrl, this@insert)
}
