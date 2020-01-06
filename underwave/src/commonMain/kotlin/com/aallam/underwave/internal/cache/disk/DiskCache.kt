package com.aallam.underwave.internal.cache.disk

import com.aallam.underwave.internal.cache.Cache
import com.aallam.underwave.internal.image.Bitmap

/**
 * Disk [Cache] implementation using a bounded amount of space on a filesystem.
 **/
internal interface DiskCache : Cache<String, Bitmap> {

    /**
     * Force buffered operations to the filesystem.
     */
    fun flush()
}
