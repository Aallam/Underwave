package com.aallam.underwave.internal.cache.memory.bitmap

import android.graphics.BitmapFactory
import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.internal.image.Config
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.lang.ref.SoftReference
import kotlin.test.assertNotNull

@RunWith(RobolectricTestRunner::class)
internal actual class BitmapPoolTest {

    @Test
    actual fun testPut() {
        val bitmapsSet: MutableSet<SoftReference<Bitmap>> = mockk(relaxed = true)
        val bitmapPool = BitmapDataPool(bitmapsSet)
        val bitmap: Bitmap = mockk()
        every { bitmap.isMutable } returns true
        every { bitmap.isRecycled } returns false

        bitmapPool.put(bitmap)

        verify { bitmapsSet.add(any()) }
    }

    @Test
    actual fun testAddInBitmapOptions() {
        val options = BitmapFactory.Options().apply { inSampleSize = 1 }
        val bitmap: Bitmap = Bitmap.createBitmap(100, 100, Config.ARGB_8888)
        val bitmapRef = SoftReference(bitmap)
        val bitmapsSet: MutableSet<SoftReference<Bitmap>> = hashSetOf(bitmapRef)
        val bitmapPool = BitmapDataPool(bitmapsSet)

        bitmapPool.addInBitmapOptions(options)

        assertNotNull(options.inBitmap)
    }
}
