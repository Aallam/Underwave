package com.aallam.underwave.internal

import android.content.Context
import com.aallam.underwave.Underwave
import com.aallam.underwave.internal.async.DefaultExceptionHandler
import com.aallam.underwave.internal.async.UnderwaveScope
import com.aallam.underwave.internal.cache.ImageCache
import com.aallam.underwave.internal.cache.ImageDataCache
import com.aallam.underwave.internal.cache.disk.DiskCache
import com.aallam.underwave.internal.cache.disk.DiskDataCache
import com.aallam.underwave.internal.cache.memory.MemoryCache
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapDataPool
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapLruCache
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapPool
import com.aallam.underwave.internal.extension.bytesToKilobytes
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
import kotlinx.coroutines.Dispatchers

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
        val scope: CoroutineScope = newCoroutineScope()
        val bitmapPool: BitmapPool = BitmapDataPool.newInstance()
        val cache: ImageCache = newImageCache(context, bitmapPool)
        val downloader: Downloader = newDownloader()
        val viewManager: ViewManager = newViewManager(bitmapPool)
        return Underwave(scope, cache, downloader, viewManager)
    }

    private fun newCoroutineScope(): CoroutineScope {
        return UnderwaveScope(Dispatchers.Main, DefaultExceptionHandler)
    }

    /**
     * Get a new [Downloader] instance.
     */
    private fun newDownloader(): Downloader {
        val httpClient: HttpClient<Bitmap> = BitmapHttpClient.newInstance()
        return ImageDownloader(httpClient)
    }

    /**
     * Get a new [ImageCache] instance.
     */
    private fun newImageCache(context: Context, bitmapPool: BitmapPool): ImageCache {
        val memoryCacheSize: Int = (Runtime.getRuntime().maxMemory() / 8).bytesToKilobytes
        val diskCacheSize = 100 * 1024L * 100L // 100mb
        val bitmapLruCache = BitmapLruCache(memoryCacheSize, bitmapPool)
        val memoryCache = MemoryCache(bitmapPool, bitmapLruCache)
        val diskCache: DiskCache = DiskDataCache.newInstance(context, diskCacheSize)
        return ImageDataCache(memoryCache, diskCache)
    }

    /**
     * Get a new [ViewManager] instance.
     */
    private fun newViewManager(bitmapPool: BitmapPool): ViewManager {
        val loader: Loader = BitmapLoader.newInstance()
        return ImageViewManager.newInstance(loader, bitmapPool)
    }
}
