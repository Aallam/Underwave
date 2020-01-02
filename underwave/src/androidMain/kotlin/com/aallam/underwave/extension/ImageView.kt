package com.aallam.underwave.extension

import android.widget.ImageView
import com.aallam.underwave.Underwave
import com.aallam.underwave.load.Request

/**
 * Load the given image url to this [ImageView].
 *
 * @param imageUrl image url to be loaded.
 */
fun ImageView.load(imageUrl: String): Request {
    return Underwave.with(this.context.applicationContext).load(imageUrl, this)
}
