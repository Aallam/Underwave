package com.aallam.underwave.storage.memory

import com.aallam.underwave.image.Bitmap
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

abstract class MemoryCacheTest {

    protected abstract val mockImage: Bitmap
    protected val mockImageSize = mockImage.getByteCount().toLong()

    @Test
    fun testPutAndGetSuccess() {
        val memoryCache = MemoryCache(mockImageSize)
        val key = "image1"
        memoryCache.put(key, mockImage)
        assertNotNull(memoryCache.get(key))
    }

    @Test
    fun testGetNotExisting() {
        val memoryCache = MemoryCache(mockImageSize)
        assertNull(memoryCache.get("image1"))
    }

    @Test
    fun testSizeEmpty() {
        val memoryCache = MemoryCache(mockImageSize)
        assertEquals(0, memoryCache.size())
    }

    @Test
    fun testSizeFull() {
        val memoryCache = MemoryCache(mockImageSize)
        memoryCache.put("image1", mockImage)
        assertEquals(mockImageSize, memoryCache.size())
    }

    @Test
    fun testClear() {
        val memoryCache = MemoryCache(mockImageSize)
        memoryCache.put("image1", mockImage)
        memoryCache.clear()
        assertEquals(0, memoryCache.size())
    }
}
