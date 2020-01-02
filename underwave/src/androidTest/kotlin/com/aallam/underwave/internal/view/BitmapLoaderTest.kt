package com.aallam.underwave.internal.view

import android.graphics.Bitmap
import com.aallam.underwave.extension.MainCoroutineRule
import com.aallam.underwave.extension.runBlocking
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapPool
import com.aallam.underwave.internal.image.Dimension
import com.aallam.underwave.internal.view.impl.BitmapLoader
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
internal actual class BitmapLoaderTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()
    @RelaxedMockK
    lateinit var bitmapPool: BitmapPool

    private lateinit var bitmapLoader: BitmapLoader

    @Before
    fun init() {
        MockKAnnotations.init(this)
        bitmapLoader = BitmapLoader(coroutineRule.testDispatcher)
    }

    @Test
    actual fun testScale() = coroutineRule.runBlocking {
        val initDimension = Dimension(200, 200)
        val bitmap: Bitmap =
            Bitmap.createBitmap(initDimension.width, initDimension.height, Bitmap.Config.ARGB_8888)
        val targetDimension = Dimension(100, 100)
        val scaledBitmap = bitmapLoader.scale(bitmap, targetDimension, bitmapPool)
        assertEquals(scaledBitmap?.height, targetDimension.height)
        assertEquals(scaledBitmap?.width, targetDimension.width)
    }
}
