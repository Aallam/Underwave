package com.aallam.underwave

import android.content.Context
import android.os.Handler
import android.os.Process
import com.aallam.underwave.image.Bitmap
import com.aallam.underwave.image.Display
import com.aallam.underwave.image.ImageView
import com.aallam.underwave.image.scale
import com.aallam.underwave.image.toDisplay
import com.aallam.underwave.network.download
import com.aallam.underwave.storage.disk.DiskCache
import com.aallam.underwave.storage.disk.getDiskCacheDirectory
import com.aallam.underwave.storage.memory.MemoryCache
import java.util.Collections.synchronizedMap
import java.util.WeakHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

actual class Underwave private constructor(context: Context) {
    private val memoryCacheSize: Long = Runtime.getRuntime().maxMemory() / 8 // eighth of max memory
    private val memoryCache: MemoryCache

    private val diskCacheSize: Long = DEFAULT_DISK_CACHE_SIZE
    private val diskCache: DiskCache

    private val handler: Handler = Handler()
    private val executorService: ExecutorService

    private val display: Display = context.resources.displayMetrics.toDisplay()
    private val imageViewMap: MutableMap<ImageView, String> = synchronizedMap(WeakHashMap<ImageView, String>())

    init {
        this.memoryCache = MemoryCache(memoryCacheSize)
        this.diskCache = DiskCache(context.getDiskCacheDirectory(), diskCacheSize)
        this.executorService = Executors.newFixedThreadPool(5) {
            Thread(it).apply {
                name = THREAD_NAME
                priority = Process.THREAD_PRIORITY_BACKGROUND
            }
        }
    }

    actual fun load(imageUrl: String, imageView: ImageView) {
        require(imageUrl.isNotEmpty()) { "Underwave:load - Image Url should not be empty" }
        imageView.setImageResource(0)
        imageViewMap[imageView] = imageUrl

        val loadRequest = LoadRequest(imageUrl, imageView)

        bitmapFromCache(imageUrl)?.let { bitmap ->
            loadBitmapIntoImageView(loadRequest, bitmap)
            return
        }

        executorService.submit {
            val bitmap: Bitmap? = loadRequest.downloadAndCache()
            if (bitmap != null) handlerPost(loadRequest, bitmap)
        }
    }

    /**
     * Load a given [Bitmap] to the requested [ImageView].
     */
    @Synchronized
    private fun loadBitmapIntoImageView(loadRequest: LoadRequest, bitmap: Bitmap) {
        val scaledBitmap: Bitmap = bitmap.scale(loadRequest.imageView.width, loadRequest.imageView.height) ?: return
        if (!isImageViewReused(loadRequest)) loadRequest.imageView.setImageBitmap(scaledBitmap)
    }

    /**
     * Download the image and cache it.
     */
    private fun LoadRequest.downloadAndCache(): Bitmap? {
        if (isImageViewReused(this)) return null
        return imageUrl.download(display.width, display.height)?.apply {
            diskCache.put(imageUrl, this)
            memoryCache.put(imageUrl, this)
        }
    }

    /**
     * Post to the handler the bitmap loading into the image view operation.
     */
    private fun handlerPost(loadRequest: LoadRequest, bitmap: Bitmap) {
        if (isImageViewReused(loadRequest)) return
        handler.post {
            if (isImageViewReused(loadRequest)) return@post
            loadBitmapIntoImageView(loadRequest, bitmap)
        }
    }

    /**
     * Check if an image has already has been loaded to the [ImageView].
     */
    private fun isImageViewReused(loadRequest: LoadRequest): Boolean {
        val imageUrl: String? = imageViewMap[loadRequest.imageView]
        return imageUrl == null || imageUrl != loadRequest.imageUrl
    }

    /**
     * Get the [Bitmap] corresponding to the given [imageUrl] if exists, otherwise returns null.
     */
    @Synchronized
    private fun bitmapFromCache(imageUrl: String): Bitmap? {
        return memoryCache.get(imageUrl) ?: bitmapFromDiskCache(imageUrl)
    }

    /**
     * Get the [Bitmap] corresponding to the given [imageUrl] if exists and push it to the memory cache.
     * Returns null if no value is cached.
     */
    private fun bitmapFromDiskCache(imageUrl: String): Bitmap? {
        return diskCache.get(imageUrl)?.let {
            memoryCache.put(imageUrl, it)
            return@let it
        }
    }

    actual companion object {
        actual val PLATFORM: String = "Android"
        private const val THREAD_NAME = "Underwave Thread"
        private const val DEFAULT_DISK_CACHE_SIZE: Long = 1024L * 1024L * 50L // 50mb

        @Synchronized
        fun with(context: Context): Underwave {
            return Underwave(context)
        }
    }
}