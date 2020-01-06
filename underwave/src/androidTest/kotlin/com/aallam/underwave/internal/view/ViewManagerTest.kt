package com.aallam.underwave.internal.view

import android.os.Handler
import com.aallam.underwave.extension.MainCoroutineRule
import com.aallam.underwave.extension.runBlocking
import com.aallam.underwave.internal.cache.memory.bitmap.BitmapPool
import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.internal.image.Dimension
import com.aallam.underwave.internal.image.ImageView
import com.aallam.underwave.internal.image.dimension
import com.aallam.underwave.internal.view.impl.ImageViewManager
import com.aallam.underwave.load.impl.LoadRequest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
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
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.lang.ref.WeakReference

@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
internal actual class ViewManagerTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    lateinit var viewManager: ViewManager
    @RelaxedMockK
    lateinit var viewMap: HashMap<ImageView, String>
    @RelaxedMockK
    lateinit var handler: Handler
    @RelaxedMockK
    lateinit var bitmapPool: BitmapPool
    @RelaxedMockK
    lateinit var loader: Loader
    @RelaxedMockK
    lateinit var imageView: ImageView

    @Before
    fun init() {
        MockKAnnotations.init(this)
        viewManager = ImageViewManager(viewMap, handler, bitmapPool, loader)
        mockkStatic("com.aallam.underwave.internal.image.DimensionKt")
    }

    @Test
    actual fun testLoad() = coroutineRule.runBlocking {
        val loadRequest: LoadRequest = mockk()
        val bitmap: Bitmap = mockk()
        val imageViewDimension = Dimension(100, 100)
        val imageViewRef = WeakReference(imageView)
        val imageUrl = "URL"
        every { loadRequest.imageView } returns imageViewRef
        every { loadRequest.imageUrl } returns imageUrl
        every { imageView.dimension } returns imageViewDimension
        every { viewMap[imageView] } returns imageUrl
        coEvery { loader.scale(bitmap, imageViewDimension, bitmapPool) } returns bitmap

        viewManager.load(loadRequest, bitmap)

        coVerify { loader.scale(bitmap, imageViewDimension, bitmapPool) }
        verify { handler.post(any()) }
    }

    @Test
    actual fun testViewReused() {
        val imageUrl = "URL"
        val loadRequest: LoadRequest = mockk()
        val imageViewRef = WeakReference(imageView)
        every { viewMap[imageView] } returns imageUrl
        every { loadRequest.imageView } returns imageViewRef
        every { loadRequest.imageUrl } returns imageUrl

        viewManager.isViewReused(loadRequest)

        verify { viewMap[imageView] }
        verify { loadRequest.imageView }
    }

    @After
    fun finish() {
        unmockkAll()
    }
}
