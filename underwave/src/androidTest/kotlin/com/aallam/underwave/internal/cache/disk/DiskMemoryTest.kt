package com.aallam.underwave.internal.cache.disk

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import com.aallam.underwave.extension.md5
import com.aallam.underwave.internal.cache.disk.DiskDataCache.Companion.openDiskLruCache
import com.jakewharton.disklrucache.DiskLruCache
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class DiskMemoryTest {

    private val diskCache: DiskCache
        get() = DiskDataCache(diskLruCache, CompressFormat.JPEG, 70)

    @RelaxedMockK
    lateinit var diskLruCache: DiskLruCache
    @RelaxedMockK
    lateinit var bitmap: Bitmap
    @RelaxedMockK
    lateinit var snapshot: DiskLruCache.Snapshot

    private val key = "image1"
    private val hash = key.md5()

    @Before
    fun init() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testPutNotExists() {
        every { diskLruCache.get(hash) } returns null
        diskCache.put(key, bitmap)
        verify { diskLruCache.edit(hash) }
    }

    @Test
    fun testPutExists() {
        every { diskLruCache.get(hash) } returns snapshot
        diskCache.put(key, bitmap)
        verify { diskLruCache.edit(hash) wasNot Called }
    }

    @Test
    fun testGet() {
        diskCache[key]
        verify { diskLruCache[hash] }
    }

    @Test
    fun testContains() {
        diskCache.contains(key)
        verify { diskLruCache[hash] }
    }

    @Test
    fun testSize() {
        diskCache.size()
        verify { diskLruCache.size() }
    }

    @Test
    fun testClear() {
        val mockSize: Long = 100
        val mockDirectory = mockk<Directory>(relaxed = true)

        every { diskLruCache.maxSize } returns mockSize
        every { diskLruCache.directory } returns mockDirectory
        mockkObject(DiskDataCache.Companion).run {
            every {
                openDiskLruCache(
                    mockDirectory,
                    mockSize
                )
            }
        } returns diskLruCache

        diskCache.clear()
        verify { diskLruCache.delete() }
    }

    @After
    fun finish() {
        unmockkAll()
    }
}
