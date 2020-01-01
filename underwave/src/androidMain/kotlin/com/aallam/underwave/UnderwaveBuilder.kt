package com.aallam.underwave

import android.content.Context
import com.aallam.underwave.internal.cache.ImageCache
import com.aallam.underwave.internal.cache.impl.ImageDataCache
import com.aallam.underwave.internal.network.Downloader
import com.aallam.underwave.internal.network.impl.ImageDownloader
import com.aallam.underwave.internal.view.ViewManager
import com.aallam.underwave.internal.view.impl.ImageViewManager

/**
 * A builder class for setting default structural classes for Underwave to use.
 * */
internal data class UnderwaveBuilder(
    private var context: Context,
    private var imageCache: ImageDataCache? = null,
    private var downloader: ImageDownloader? = null,
    private var viewManager: ViewManager? = null
) {

    /**
     * Sets the [ImageDataCache].
     *
     * @param imageCache image cache to be set.
     * @return This builder.
     */
    fun imageCache(imageCache: ImageDataCache) = apply { this.imageCache = imageCache }

    /**
     * Sets the [ImageDownloader].
     *
     * @param downloader data downloader to be set.
     * @return This builder.
     */
    fun downloader(downloader: ImageDownloader) = apply { this.downloader = downloader }

    /**
     * Build an [Underwave].
     *
     * @return an [Underwave] instance
     */
    fun build(): Underwave {
        val cache: ImageCache = imageCache ?: ImageDataCache.newInstance(context)
        val viewManager: ViewManager = viewManager ?: ImageViewManager.newInstance(context)
        val downloader: Downloader = downloader ?: ImageDownloader.newInstance(cache, viewManager)
        return Underwave(cache, downloader, viewManager)
    }
}
