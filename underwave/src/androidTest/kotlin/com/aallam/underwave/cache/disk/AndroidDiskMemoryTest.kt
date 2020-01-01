package com.aallam.underwave.cache.disk

import android.content.Context
import android.graphics.Bitmap
import androidx.test.core.app.ApplicationProvider
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
class AndroidDiskMemoryTest : DiskMemoryTest() {

    override val mockImage: Bitmap
        get() = Bitmap.createBitmap(4096, 2160, Bitmap.Config.ARGB_8888)

    override val compressedImageSize: Long = diskCompressedImageSize()
    override val diskCache: DiskCache = newDiskCache(compressedImageSize)

    private fun diskCompressedImageSize(): Long {
        val tmpDiskCache = newDiskCache(mockImage.byteCount.toLong())
        tmpDiskCache.put("simple", mockImage)
        return tmpDiskCache.size().also {
            tmpDiskCache.clear()
        }
    }

    private fun newDiskCache(size: Long): DiskCache {
        val context: Context = ApplicationProvider.getApplicationContext()
        val diskCacheDirectory: File = context.getDiskCacheDirectory("underwave-cache")
        return DiskCache(diskCacheDirectory, size)
    }

    @Test
    fun leastRecentlyUsed() {
        val diskCache = newDiskCache(compressedImageSize * 2)
        diskCache.put("image1", mockImage)
        diskCache.put("image2", mockImage)
        diskCache.put("image3", mockImage)
        diskCache.flush()
        assertNull(diskCache.get("image1"))
        assertNotNull(diskCache.get("image2"))
    }


    @AfterTest
    fun clean() {
        diskCache.clear()
    }
}