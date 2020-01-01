package com.aallam.underwave.internal.cache.disk

import com.aallam.underwave.internal.cache.Cache

/**
 * Disk [Cache] implementation using a bounded amount of space on a filesystem.
 **/
internal expect class DiskDataCache : DiskCache
