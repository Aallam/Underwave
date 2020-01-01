package com.aallam.underwave.storage.memory

import android.graphics.Bitmap
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
class AndroidMemoryCacheTest : MemoryCacheTest() {
    override val mockImage: Bitmap
        get() = Bitmap.createBitmap(4096, 2160, Bitmap.Config.ARGB_8888)

    @Test
    fun leastRecentlyUsed() {
        val memoryCache = MemoryCache(mockImageSize * 2)
        memoryCache.put("image1", mockImage)
        memoryCache.put("image2", mockImage)
        memoryCache.put("image3", mockImage)
        assertNull(memoryCache.get("image1"))
        assertNotNull(memoryCache.get("image2"))
    }
}