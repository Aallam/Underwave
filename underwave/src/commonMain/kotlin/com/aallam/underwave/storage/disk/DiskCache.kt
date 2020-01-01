package com.aallam.underwave.storage.disk

import com.aallam.underwave.Bitmap
import com.aallam.underwave.storage.Cache

/**
 * Disk [Cache] implementation using a bounded amount of space on a filesystem.
 *
 * @param directory writable cache directory.
 * @param size the maximum number of bytes this cache should use to store.
 * @param compressFormat the format of the compressed images.
 * @param compressQuality  Hint to the compressor, 0-100.
 **/
expect class DiskCache(
    directory: Directory,
    size: Long,
    compressFormat: CompressFormat = CompressFormat.JPEG,
    compressQuality: Int = 70
) : Cache<String, Bitmap>
