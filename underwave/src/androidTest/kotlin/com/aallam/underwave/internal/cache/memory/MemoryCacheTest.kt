package com.aallam.underwave.internal.cache.memory

import com.aallam.underwave.extension.MainCoroutineRule
import com.aallam.underwave.extension.runBlocking
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapLruCache
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapPool
import com.aallam.underwave.internal.image.Bitmap
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@ExperimentalCoroutinesApi
internal actual class MemoryCacheTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var memoryCache: MemoryCache
    @RelaxedMockK
    lateinit var bitmapPool: BitmapPool
    @RelaxedMockK
    lateinit var bitmapLruCache: BitmapLruCache
    @RelaxedMockK
    lateinit var bitmap: Bitmap

    @Before
    fun init() {
        MockKAnnotations.init(this)
        memoryCache = MemoryCache(bitmapPool, bitmapLruCache)
    }

    @Test
    actual fun testGet() = coroutineRule.runBlocking {
        val key = "url"
        every { bitmapLruCache.get(key) } returns bitmap
        val result = memoryCache.get(key)
        verify { bitmapLruCache.get(key) }
        assertEquals(bitmap, result)
    }

    @Test
    actual fun testPut() = coroutineRule.runBlocking {
        val key = "url"
        memoryCache.put(key, bitmap)
        verify { bitmapLruCache.put(key, bitmap) }
    }

    @Test
    actual fun testGetNotExisting() = coroutineRule.runBlocking {
        val key = "url"
        every { bitmapLruCache.get(key) } returns null
        assertNull(memoryCache.get(key))
    }

    @Test
    actual fun testSize() = coroutineRule.runBlocking {
        memoryCache.clear()
        verify { bitmapLruCache.evictAll() }
        verify { bitmapPool.clear() }
    }

    @After
    fun finish() {
        unmockkAll()
    }
}
