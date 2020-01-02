package com.aallam.underwave.internal.cache.disk

import android.content.Context
import android.graphics.BitmapFactory
import com.aallam.underwave.internal.async.UnderwaveDispatchers
import com.aallam.underwave.internal.cache.Cache
import com.aallam.underwave.internal.extension.bytesToKilobytes
import com.aallam.underwave.internal.extension.log
import com.aallam.underwave.internal.extension.md5
import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.internal.image.CompressFormat
import com.jakewharton.disklrucache.DiskLruCache
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Disk [Cache] implementation using a bounded amount of space on a filesystem.
 *
 * @param diskLruCache a cache that uses a bounded amount of space on a filesystem.
 * @param compressFormat the format of the compressed images.
 * @param compressQuality hint to the compressor, 0-100.
 **/
internal actual class DiskDataCache(
    private var diskLruCache: DiskLruCache,
    private val compressFormat: CompressFormat,
    private val compressQuality: Int,
    private val dispatcher: CoroutineDispatcher
) : DiskCache {

    override suspend fun get(key: String): Bitmap? {
        return withContext(dispatcher) {
            val hash = key.md5()
            diskLruCache[hash]?.use { snapshot ->
                log("get from disk cache: $key")
                snapshot.getInputStream(0)?.buffered()?.let { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
            }
        }
    }

    override suspend fun put(key: String, value: Bitmap): Unit = coroutineScope<Unit> {
        launch(dispatcher) {
            val hash = key.md5()
            log("write $key to cache: $hash")
            diskLruCache.edit(hash)?.let { editor -> writeBitmapToDisk(editor, value) }
        }
    }

    /**
     * Write [Bitmap] to disk.
     *
     * @param bitmap Image to be saved.
     * @param editor DiskLruCache editor.
     */
    private fun writeBitmapToDisk(
        editor: DiskLruCache.Editor,
        bitmap: Bitmap
    ) {
        try {
            val isSuccess: Boolean = editor.newOutputStream(0).use {
                bitmap.compress(compressFormat, compressQuality, it.buffered())
            }
            if (isSuccess) editor.flushAndCommit() else editor.abortEdit()
        } catch (e: IOException) {
            editor.abortEdit()
        }
    }

    /**
     * Force buffered operations to the filesystem and commit edit so it is visible to readers.
     */
    private fun DiskLruCache.Editor.flushAndCommit() {
        diskLruCache.flush()
        commit()
    }

    /**
     * Abort the edit and ignore if the operation fails.
     */
    private fun DiskLruCache.Editor.abortEdit() {
        try {
            abort()
        } catch (_: IOException) {
            // ignored exception
        }
    }

    override fun size(): Long {
        return diskLruCache.size()
    }

    override suspend fun clear() {
        log("clear disk: ${diskLruCache.size().bytesToKilobytes} kb")
        val directory: Directory = diskLruCache.directory
        val maxSize: Long = diskLruCache.maxSize
        diskLruCache.maxSize = 0 // for full delete maxsize must be 0 otherwise will trim to size.
        reset(directory, maxSize)
    }

    /**
     * Delete cache, delete operation will close the cache, thus re-open it.
     */
    private suspend fun reset(directory: Directory, size: Long): Unit = coroutineScope<Unit> {
        launch(dispatcher) {
            diskLruCache.delete()
            diskLruCache = DiskLruCache.open(directory, VERSION, COUNT, size)
        }
    }

    /**
     * Force buffered operations to the filesystem.
     */
    override fun flush() {
        diskLruCache.flush()
    }

    companion object {
        private const val VERSION = 1
        private const val COUNT = 1

        private val DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG
        private const val DEFAULT_COMPRESS_QUALITY = 70

        /**
         * Creates a new [DiskDataCache] object.
         */
        @JvmStatic
        fun newInstance(
            context: Context,
            size: Long,
            dispatcher: CoroutineDispatcher = UnderwaveDispatchers.IO
        ): DiskDataCache = runBlocking {
            val diskLruCache = withContext(dispatcher) {
                val directory: Directory = context.getDiskCacheDirectory()
                DiskLruCache.open(directory, VERSION, COUNT, size)
            }
            DiskDataCache(
                diskLruCache = diskLruCache,
                compressFormat = DEFAULT_COMPRESS_FORMAT,
                compressQuality = DEFAULT_COMPRESS_QUALITY,
                dispatcher = dispatcher
            )
        }
    }
}
