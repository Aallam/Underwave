package com.aallam.underwave.internal.cache

import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.internal.cache.disk.DiskDataCache
import com.aallam.underwave.internal.cache.memory.MemoryCache
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal abstract class ImageCacheTest {

    private val tag = "image"

    @MockK
    lateinit var bitmap: Bitmap
    @MockK
    lateinit var memoryCache: MemoryCache
    @MockK
    lateinit var diskCache: DiskDataCache

    abstract val imageCache: ImageDataCache

    @BeforeTest
    fun init() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testPut() {
        every { memoryCache.put(tag, bitmap) } just Runs
        every { diskCache.put(tag, bitmap) } just Runs
        imageCache.put(tag, bitmap)
        verify { memoryCache.put(tag, bitmap) }
        verify { diskCache.put(tag, bitmap) }
    }

    @Test
    fun testGetFromMemory() {
        every { memoryCache[tag] } returns bitmap
        val result = imageCache[tag]
        verify { memoryCache[tag] }
        assertEquals(bitmap, result)
    }

    @Test
    fun testGetFromDisk() {
        every { memoryCache[tag] } returns null
        every { diskCache[tag] } returns bitmap
        every { memoryCache.put(tag, bitmap) } just Runs
        val result = imageCache[tag]
        verify { memoryCache[tag] }
        verify { diskCache[tag] }
        assertEquals(bitmap, result)
    }

    @Test
    fun testClean() {
        every { memoryCache.clear() } just Runs
        every { diskCache.clear() } just Runs
        imageCache.clear()
        verify { memoryCache.clear() }
        verify { diskCache.clear() }
    }
}
