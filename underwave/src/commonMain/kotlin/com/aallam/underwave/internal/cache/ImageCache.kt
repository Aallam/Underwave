package com.aallam.underwave.internal.cache

import com.aallam.underwave.internal.cache.memory.bitmap.BitmapPool
import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.load.impl.LoadRequest

/**
 * Image cache repository.
 */
internal interface ImageCache {

    val bitmapPool: BitmapPool

    /**
     * Cache given url and bitmap in the memory and disk cache.
     *
     * @param url image url
     * @param bitmap image bitmap to cache.
     */
    fun put(url: String, bitmap: Bitmap)

    /**
     * Get the bitmap corresponding to the given url.
     * Looks first at the memory cache, if nothing exists looks at disk cache, if a cache
     * has been found in the disk, its pushed to the memory cache.
     * If nothing found, return null.
     *
     * @param imageUrl image url to look for.
     * @return cached bitmap if exists, otherwise null
     */
    operator fun get(imageUrl: String): Bitmap?

    /**
     * Clears as much memory and as possible and disk cache.
     */
    fun clear()

    /**
     * Load an image from cache, calls [onSuccess] if exists, otherwise calls [onFailure]
     */
    suspend fun load(
        loadRequest: LoadRequest,
        onSuccess: suspend (Bitmap) -> Unit,
        onFailure: suspend () -> Unit
    )
}
