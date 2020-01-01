package com.aallam.underwave.cache.disk

import com.aallam.underwave.image.Bitmap
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

abstract class DiskMemoryTest {
    internal abstract val diskCache: DiskCache
    internal abstract val mockImage: Bitmap
    internal abstract val compressedImageSize: Long

    @Test
    fun testPutAndGetSuccess() {
        val key = "image1"
        diskCache.put(key, mockImage)
        assertNotNull(diskCache.get(key))
    }

    @Test
    fun testGetNotExisting() {
        assertNull(diskCache.get("image1"))
    }

    @Test
    fun testSizeEmpty() {
        assertEquals(0, diskCache.size())
    }

    @Test
    fun testSizeFull() {
        println("before ${diskCache.size()}")
        diskCache.put("image1", mockImage)
        assertEquals(compressedImageSize, diskCache.size())
    }

    @Test
    fun testClear() {
        diskCache.put("image1", mockImage)
        diskCache.put("image2", mockImage)
        diskCache.put("image3", mockImage)
        diskCache.clear()
        assertEquals(0, diskCache.size())
    }
}