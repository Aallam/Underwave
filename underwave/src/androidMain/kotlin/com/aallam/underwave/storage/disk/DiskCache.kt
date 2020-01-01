package com.aallam.underwave.storage.disk

import android.graphics.BitmapFactory
import com.aallam.underwave.image.Bitmap
import com.aallam.underwave.storage.Cache
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
    directory: Directory,
    size: Long,
    private val compressFormat: CompressFormat,
    private val compressQuality: Int
) : Cache<String, Bitmap> {

    private val diskLruCache: DiskLruCache = DiskLruCache.open(directory, VERSION, COUNT, size)

    override fun get(key: String): Bitmap? {
        val snapshot: DiskLruCache.Snapshot = diskLruCache[key] ?: return null
        snapshot.use {
            val input: InputStream = snapshot.getInputStream(0) ?: return null
            val buffIn = BufferedInputStream(input, IO_BUFFER_SIZE)
            return BitmapFactory.decodeStream(buffIn)
        }
    }

    override fun put(key: String, value: Bitmap) {
        val editor: DiskLruCache.Editor = diskLruCache.edit(key) ?: return
        try {
            val isSuccess: Boolean = writeBitmapToDisk(value, editor)
            if (isSuccess) editor.flushAndCommit() else editor.abortEdit()
        } catch (e: IOException) {
            editor.abortEdit()
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
        return BufferedOutputStream(editor.newOutputStream(0), IO_BUFFER_SIZE).use {
            bitmap.compress(compressFormat, compressQuality, it)
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
        diskLruCache.maxSize = 0 // for full delete maxsize should be 0, because delete operation always trim to size
        diskLruCache.delete() // delete will close the cache.
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