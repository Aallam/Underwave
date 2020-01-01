package com.aallam.underwave.internal.cache.impl

import com.aallam.underwave.internal.cache.ImageCache
import com.aallam.underwave.internal.cache.disk.DiskDataCache
import com.aallam.underwave.internal.cache.memory.MemoryCache

/**
 * Image cache repository.
 */
internal expect class ImageDataCache(
    memoryCache: MemoryCache,
    diskCache: DiskDataCache
) : ImageCache
