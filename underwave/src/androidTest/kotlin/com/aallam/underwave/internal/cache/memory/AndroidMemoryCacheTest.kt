package com.aallam.underwave.internal.cache.memory

import android.util.LruCache
import com.aallam.underwave.image.Bitmap
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class AndroidMemoryCacheTest {

    private val memoryCache: MemoryCache
        get() = MemoryCache(lruCache)

    @RelaxedMockK
    lateinit var lruCache: LruCache<String, Bitmap>
    @RelaxedMockK
    lateinit var bitmap: Bitmap

    private val key = "image1"

    @Before
    fun init() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testPut() {
        val cache = memoryCache
        cache.put(key, bitmap)
        verify { lruCache.put(key, bitmap) }
    }

    @Test
    fun testGet() {
        val cache = memoryCache
        every { lruCache.get(key) } returns bitmap
        val bitmap = cache[key]
        verify { lruCache[key] }
        assertEquals(bitmap, bitmap)
    }

    @Test
    fun testContains() {
        memoryCache.contains(key)
        verify { lruCache.get(key) }
    }

    @Test
    fun testGetNotExisting() {
        every { lruCache.get(key) } returns null
        assertNull(memoryCache[key])
    }

    @Test
    fun testSize() {
        memoryCache.clear()
        verify { lruCache.evictAll() }
    }

    @After
    fun finish() {
        unmockkAll()
    }
}
