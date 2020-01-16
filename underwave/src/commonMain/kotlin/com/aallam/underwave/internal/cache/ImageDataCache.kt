package com.aallam.underwave.internal.cache

import com.aallam.underwave.internal.cache.disk.DiskCache
import com.aallam.underwave.internal.cache.disk.DiskDataCache
import com.aallam.underwave.internal.cache.memory.MemoryCache

/**
 * Image cache repository.
 *
 * @param memoryCache in-memory cache
 * @param diskCache filesystem cache
 */
internal expect class ImageDataCache(
    memoryCache: MemoryCache,
    diskCache: DiskCache
) : ImageCache
