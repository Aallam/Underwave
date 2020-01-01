package com.aallam.underwave

import com.aallam.underwave.image.ImageView

/**
 * Class representing an image loading request.
 */
internal data class LoadRequest(val imageUrl: String, val imageView: ImageView)