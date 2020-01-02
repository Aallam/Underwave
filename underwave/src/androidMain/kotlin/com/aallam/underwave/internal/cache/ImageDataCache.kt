package com.aallam.underwave.internal.cache

import android.content.Context
import com.aallam.underwave.internal.cache.disk.DiskDataCache
import com.aallam.underwave.internal.cache.memory.MemoryCache
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapPool
import com.aallam.underwave.internal.async.dispatcher.impl.SourceExecutor
import com.aallam.underwave.internal.extension.bytesToKilobytes
import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.load.impl.LoadRequest
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Image cache repository.
 */
internal actual class ImageDataCache actual constructor(
    val memoryCache: MemoryCache,
    val diskCache: DiskDataCache
) : ImageCache {

    override val bitmapPool: BitmapPool
        get() = memoryCache.bitmapPool

    override fun put(url: String, bitmap: Bitmap) {
        diskCache.put(url, bitmap)
        memoryCache.put(url, bitmap)
    }

    override operator fun get(imageUrl: String): Bitmap? {
        return memoryCache[imageUrl] ?: diskCache[imageUrl]?.let { bitmap ->
            memoryCache.put(imageUrl, bitmap)
            return@let bitmap
        }
    }

    override suspend fun load(
        loadRequest: LoadRequest,
        onSuccess: suspend (Bitmap) -> Unit,
        onFailure: suspend () -> Unit
    ): Unit = coroutineScope {
        val url = loadRequest.imageUrl
        memoryCache[url]?.let {
            onSuccess(it)
        } ?: fromDisk(url, onSuccess, onFailure)
    }

    private suspend fun fromDisk(
        url: String,
        onSuccess: suspend (Bitmap) -> Unit,
        onFailure: suspend () -> Unit
    ): Unit = coroutineScope<Unit> {
        launch(SourceExecutor.dispatcher) {
            diskCache[url]?.let { bitmap ->
                memoryCache.put(url, bitmap)
                onSuccess(bitmap)
            } ?: onFailure()
        }
    }

    override fun clear() {
        memoryCache.clear()
        diskCache.clear()
    }

    companion object {
        private const val DISK_CACHE_SIZE = 100 * 1024L * 100L // 100mb
        private val MEMORY_CACHE_SIZE: Int = (Runtime.getRuntime().maxMemory() / 8).bytesToKilobytes

        /**
         * Creates a new [ImageDataCache] object.
         */
        @JvmStatic
        fun newInstance(
            context: Context,
            bitmapPool: BitmapPool
        ): ImageDataCache {
            val memoryCache: MemoryCache = MemoryCache.newInstance(
                bitmapPool,
                MEMORY_CACHE_SIZE
            )
            val diskCache: DiskDataCache = DiskDataCache.newInstance(
                context,
                DISK_CACHE_SIZE
            )
            return ImageDataCache(
                memoryCache = memoryCache,
                diskCache = diskCache
            )
        }
    }
}
