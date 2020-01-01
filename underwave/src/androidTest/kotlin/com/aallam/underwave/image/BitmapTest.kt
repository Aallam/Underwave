package com.aallam.underwave.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
internal class BitmapTest {

    private val dimension: Dimension = Dimension(100, 100)

    @Test
    fun testCalculateInSampleSize() {
        val options = BitmapFactory.Options()
        options.outWidth = 200
        options.outHeight = 200

        val sampleSize = options.calculateInSampleSize(dimension)
        println(sampleSize)
        assertEquals(2, sampleSize)
    }

    @Test
    fun testBitmapScale() {
        val bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)
        val scaledBitmap = bitmap.scale(dimension)
        assertEquals(dimension.width, scaledBitmap?.width)
        assertEquals(dimension.height, scaledBitmap?.height)
    }
}