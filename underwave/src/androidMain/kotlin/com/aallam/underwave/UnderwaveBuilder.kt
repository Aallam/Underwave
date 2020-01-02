package com.aallam.underwave

import android.content.Context
import com.aallam.underwave.internal.async.UnderwaveScope
import com.aallam.underwave.internal.cache.ImageCache
import com.aallam.underwave.internal.cache.ImageDataCache
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapDataPool
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapPool
import com.aallam.underwave.internal.network.Downloader
import com.aallam.underwave.internal.network.impl.BitmapHttpClient
import com.aallam.underwave.internal.network.impl.ImageDownloader
import com.aallam.underwave.internal.view.Loader
import com.aallam.underwave.internal.view.ViewManager
import com.aallam.underwave.internal.view.impl.BitmapLoader
import com.aallam.underwave.internal.view.impl.ImageViewManager
import kotlinx.coroutines.CoroutineScope

/**
 * A builder class for setting default structural classes for Underwave to use.
 * */
internal data class UnderwaveBuilder(
    private val context: Context,
    private var scope: CoroutineScope? = null,
    private var loader: Loader? = null,
    private var bitmapPool: BitmapPool? = null,
    private var bitmapHttpClient: BitmapHttpClient? = null
) {

    /**
     * Set coroutine [scope] object.
     */
    fun scope(scope: CoroutineScope) = apply { this.scope = scope }

    /**
     * Set [loader] object.
     */
    fun loader(loader: Loader) = apply { this.loader = loader }

    /**
     * Set [BitmapPool] object.
     */
    fun bitmapPool(bitmapPool: BitmapPool) = apply { this.bitmapPool = bitmapPool }

    /**
     * Set [BitmapHttpClient] object.
     */
    fun bitmapHttpClient(bitmapHttpClient: BitmapHttpClient) =
        apply { this.bitmapHttpClient = bitmapHttpClient }

    /**
     * Build an [Underwave].
     *
     * @return an [Underwave] instance
     */
    fun build(): Underwave {
        val bitmapPool: BitmapPool = bitmapPool ?: BitmapDataPool.newInstance()
        val loader: Loader = loader ?: BitmapLoader.newInstance()
        val scope: CoroutineScope = scope ?: UnderwaveScope.newInstance()
        val bitmapHttpClient: BitmapHttpClient = bitmapHttpClient ?: BitmapHttpClient.newInstance()

        val cache: ImageCache = ImageDataCache.newInstance(context, bitmapPool)
        val viewManager: ViewManager = ImageViewManager.newInstance(loader, bitmapPool)
        val downloader: Downloader = ImageDownloader.newInstance(bitmapHttpClient)

        return Underwave(scope, cache, downloader, viewManager)
    }
}
