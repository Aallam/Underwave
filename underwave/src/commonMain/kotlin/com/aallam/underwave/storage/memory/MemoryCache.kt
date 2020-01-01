package com.aallam.underwave.storage.memory

import com.aallam.underwave.image.Bitmap
import com.aallam.underwave.storage.Cache

/**
 * A memory [Cache] implantation that holds references to a limited number of values.
 *
 * @param size the maximum sum of the sizes of the entries in the cache in bytes.
 */
internal expect class MemoryCache(
    size: Long
) : Cache<String, Bitmap>