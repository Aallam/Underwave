package com.aallam.underwave.internal.cache.memory

import com.aallam.underwave.internal.cache.Cache
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapLruCache
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapPool
import com.aallam.underwave.internal.image.Bitmap

/**
 * A memory [Cache] implantation that holds references to a limited number of values.
 *
 * @param bitmapPool pool of reusable bitmaps
 * @param bitmapLruCache bitmap cache with Least Recently Used capabilities
 */
internal expect class MemoryCache(bitmapPool: BitmapPool, bitmapLruCache: BitmapLruCache) :
    Cache<String, Bitmap>
