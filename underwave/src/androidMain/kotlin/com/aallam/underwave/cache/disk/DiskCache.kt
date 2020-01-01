package com.aallam.underwave.cache.disk

import android.graphics.BitmapFactory
import com.aallam.underwave.extension.log
import com.aallam.underwave.image.Bitmap
import com.aallam.underwave.cache.Cache
import com.aallam.underwave.extension.md5
import com.jakewharton.disklrucache.DiskLruCache
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Disk [Cache] implementation using a bounded amount of space on a filesystem.
 *
 * @param directory writable cache directory.
 * @param size the maximum number of bytes this cache should use to store.
 * @param compressFormat the format of the compressed images.
 * @param compressQuality  Hint to the compressor, 0-100.
 **/
internal actual class DiskCache actual constructor(
    private val directory: Directory,
    private val size: Long,
    private val compressFormat: CompressFormat,
    private val compressQuality: Int
) : Cache<String, Bitmap> {

    private var diskLruCache: DiskLruCache = openDiskLruCache()

    private fun openDiskLruCache() = DiskLruCache.open(directory, VERSION, COUNT, size)

    override fun get(key: String): Bitmap? {
        val urlHash = key.md5()
        val snapshot: DiskLruCache.Snapshot = diskLruCache[urlHash] ?: return null
        snapshot.use {
            val inputStream: InputStream = snapshot.getInputStream(0) ?: return null
            val bufferedInputStream = BufferedInputStream(inputStream, IO_BUFFER_SIZE)
            return BitmapFactory.decodeStream(bufferedInputStream)
        }
    }

    override fun put(key: String, value: Bitmap) {
        if (contains(key)) return
        val urlHash = key.md5()
        val editor: DiskLruCache.Editor = diskLruCache.edit(urlHash) ?: return
        try {
            log("write $key to cache: $urlHash")
            val isSuccess: Boolean = writeBitmapToDisk(value, editor)
            if (isSuccess) editor.flushAndCommit() else editor.abortEdit()
        } catch (e: IOException) {
            editor.abortEdit()
        }
    }

    override fun contains(key: String): Boolean {
        val hashedKey = key.md5()
        val snapshot: DiskLruCache.Snapshot = diskLruCache[hashedKey] ?: return false
        snapshot.use {
            return true
        }
    }

    /**
     * Write [Bitmap] to disk.
     *
     * @param bitmap Image to be saved.
     * @param editor DiskLruCache editor.
     * @return true if successfully wrote to the disk.
     */
    private fun writeBitmapToDisk(
        bitmap: Bitmap,
        editor: DiskLruCache.Editor
    ): Boolean {
        return editor.newOutputStream(0).use {
            val outputStream = BufferedOutputStream(it, IO_BUFFER_SIZE)
            bitmap.compress(compressFormat, compressQuality, outputStream)
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

    override fun clear() {
        diskLruCache.maxSize = 0  // for full delete maxsize must be 0 otherwise will trim to size.
        diskLruCache.delete() // delete will close the cache.
        this.diskLruCache = openDiskLruCache() // delete operation closes the cache, re-open it.
    }

    /**
     * Force buffered operations to the filesystem.
     */
    fun flush() {
        diskLruCache.flush()
    }

    companion object {
        private const val VERSION = 1
        private const val COUNT = 1
        private const val IO_BUFFER_SIZE = 8 * 1024 // 8kb
    }
}