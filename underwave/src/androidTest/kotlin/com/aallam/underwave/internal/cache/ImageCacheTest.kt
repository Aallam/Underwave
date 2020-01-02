package com.aallam.underwave.internal.cache

import com.aallam.underwave.extension.MainCoroutineRule
import com.aallam.underwave.extension.runBlocking
import com.aallam.underwave.internal.cache.disk.DiskDataCache
import com.aallam.underwave.internal.cache.memory.MemoryCache
import com.aallam.underwave.internal.image.Bitmap
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal actual class ImageCacheTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    lateinit var imageCache: ImageDataCache
    @RelaxedMockK
    lateinit var bitmap: Bitmap
    @RelaxedMockK
    lateinit var memoryCache: MemoryCache
    @RelaxedMockK
    lateinit var diskCache: DiskDataCache

    @Before
    fun init() {
        MockKAnnotations.init(this)
        imageCache = ImageDataCache(
            memoryCache,
            diskCache
        )
    }

    @Test
    actual fun testPut() = coroutineRule.runBlocking {
        val key = "url"
        imageCache.put(key, bitmap)
        coVerify { memoryCache.put(key, bitmap) }
        coVerify { diskCache.put(key, bitmap) }
    }

    @Test
    actual fun testGetFromMemory() = coroutineRule.runBlocking {
        val key = "url"
        coEvery { memoryCache.get(key) } returns bitmap
        val result = imageCache.get(key)
        coVerify { memoryCache.get(key) }
        assertEquals(bitmap, result)
    }

    @Test
    actual fun testGetFromDisk() = coroutineRule.runBlocking {
        val key = "url"
        coEvery { memoryCache.get(key) } returns null
        coEvery { diskCache.get(key) } returns bitmap
        val result = imageCache.get(key)
        coVerify { memoryCache.get(key) }
        coVerify { diskCache.get(key) }
        assertEquals(bitmap, result)
    }

    @Test
    actual fun testClean() = coroutineRule.runBlocking {
        imageCache.clear()
        coVerify { memoryCache.clear() }
        coVerify { diskCache.clear() }
    }
}
