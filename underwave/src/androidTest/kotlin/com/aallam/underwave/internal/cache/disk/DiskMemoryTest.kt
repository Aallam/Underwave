package com.aallam.underwave.internal.cache.disk

import android.graphics.Bitmap.CompressFormat
import com.aallam.underwave.extension.MainCoroutineRule
import com.aallam.underwave.extension.runBlocking
import com.aallam.underwave.internal.extension.md5
import com.aallam.underwave.internal.image.Bitmap
import com.jakewharton.disklrucache.DiskLruCache
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal actual class DiskMemoryTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var diskCache: DiskCache
    @RelaxedMockK
    private lateinit var diskLruCache: DiskLruCache
    @RelaxedMockK
    private lateinit var bitmap: Bitmap

    @Before
    fun init() {
        MockKAnnotations.init(this)
        diskCache =
            DiskDataCache(diskLruCache, CompressFormat.JPEG, 70, coroutineRule.testDispatcher)
    }

    @Test
    actual fun testGet() = coroutineRule.runBlocking {
        val key = "url"
        val hash = key.md5()
        diskCache.get(key)
        verify { diskLruCache[hash] }
    }

    @Test
    actual fun testPut() = coroutineRule.runBlocking {
        val key = "url"
        val hash = key.md5()
        val editor = mockk<DiskLruCache.Editor>(relaxed = true)
        every { diskLruCache.edit(hash) } returns editor
        diskCache.put(key, bitmap)
        verify { diskLruCache.edit(hash) }
    }

    @Test
    actual fun testSize() {
        diskCache.size()
        verify { diskLruCache.size() }
    }

    @Test
    actual fun testClear() = coroutineRule.runBlocking {
        val mockSize: Long = 100
        val mockDirectory = mockk<Directory>(relaxed = true)
        every { diskLruCache.maxSize } returns mockSize
        every { diskLruCache.directory } returns mockDirectory
        mockkStatic(DiskLruCache::class)
        every { DiskLruCache.open(mockDirectory, any(), any(), mockSize) } returns diskLruCache
        diskCache.clear()
        verify { diskLruCache.delete() }
    }

    @After
    fun finish() {
        unmockkAll()
    }
}
