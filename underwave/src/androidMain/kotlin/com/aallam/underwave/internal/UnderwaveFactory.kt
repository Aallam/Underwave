package com.aallam.underwave.internal

import android.content.Context
import com.aallam.underwave.Underwave
import com.aallam.underwave.internal.async.UnderwaveScope
import com.aallam.underwave.internal.cache.ImageCache
import com.aallam.underwave.internal.cache.ImageDataCache
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapDataPool
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapPool
import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.internal.network.Downloader
import com.aallam.underwave.internal.network.HttpClient
import com.aallam.underwave.internal.network.impl.BitmapHttpClient
import com.aallam.underwave.internal.network.impl.ImageDownloader
import com.aallam.underwave.internal.view.Loader
import com.aallam.underwave.internal.view.ViewManager
import com.aallam.underwave.internal.view.impl.BitmapLoader
import com.aallam.underwave.internal.view.impl.ImageViewManager
import kotlinx.coroutines.CoroutineScope

/**
 * Factory to create [Underwave] objects.
 */
internal actual object UnderwaveFactory {

    /**
     * Create a new [Underwave] object.
     *
     * @param context android context
     * @return an [Underwave] object instance
     */
    fun create(context: Context): Underwave {
        val scope: CoroutineScope = UnderwaveScope.newInstance()
        val bitmapPool: BitmapPool = BitmapDataPool.newInstance()
        val httpClient: HttpClient<Bitmap> = BitmapHttpClient.newInstance()
        val loader: Loader = BitmapLoader.newInstance()
        val cache: ImageCache = ImageDataCache.newInstance(context, bitmapPool)
        val viewManager: ViewManager = ImageViewManager.newInstance(loader, bitmapPool)
        val downloader: Downloader = ImageDownloader.newInstance(httpClient)
        return Underwave(scope, cache, downloader, viewManager)
    }
}
