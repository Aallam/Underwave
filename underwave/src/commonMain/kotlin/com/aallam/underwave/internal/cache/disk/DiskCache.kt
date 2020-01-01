package com.aallam.underwave.internal.cache.disk

import com.aallam.underwave.image.Bitmap
import com.aallam.underwave.internal.cache.Cache

/**
 * Disk [Cache] implementation using a bounded amount of space on a filesystem.
 **/
internal interface DiskCache : Cache<String, Bitmap> {

    /**
     * Force buffered operations to the filesystem.
     */
    fun flush()
}
