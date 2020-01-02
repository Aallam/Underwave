package com.aallam.underwave

import android.content.Context
import com.aallam.underwave.internal.BitmapLoader
import com.aallam.underwave.internal.cache.ImageCache
import com.aallam.underwave.internal.cache.ImageDataCache
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapDataPool
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapPool
import com.aallam.underwave.internal.network.Downloader
import com.aallam.underwave.internal.network.impl.ImageDownloader
import com.aallam.underwave.internal.view.ViewManager
import com.aallam.underwave.internal.view.impl.ImageViewManager

/**
 * A builder class for setting default structural classes for Underwave to use.
 * */
internal data class UnderwaveBuilder(
    private val context: Context
) {

    /**
     * Build an [Underwave].
     *
     * @return an [Underwave] instance
     */
    fun build(): Underwave {
        val loader = BitmapLoader()
        val bitmapPool: BitmapPool = BitmapDataPool.newInstance()
        val cache: ImageCache = ImageDataCache.newInstance(context, bitmapPool)
        val viewManager: ViewManager = ImageViewManager.newInstance(context, bitmapPool, loader)
        val downloader: Downloader = ImageDownloader.newInstance(cache, viewManager, loader)
        return Underwave(cache, downloader, viewManager)
    }
}
