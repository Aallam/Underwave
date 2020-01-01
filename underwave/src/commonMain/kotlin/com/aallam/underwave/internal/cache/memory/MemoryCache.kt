package com.aallam.underwave.internal.cache.memory

import com.aallam.underwave.image.Bitmap
import com.aallam.underwave.internal.cache.Cache

/**
 * A memory [Cache] implantation that holds references to a limited number of values.
 *
 * @param size the maximum sum of the sizes of the entries in the cache in bytes.
 */
internal expect class MemoryCache : Cache<String, Bitmap>
