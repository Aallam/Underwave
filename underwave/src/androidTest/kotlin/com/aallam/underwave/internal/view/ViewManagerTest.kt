package com.aallam.underwave.internal.view

import android.os.Handler
import com.aallam.underwave.image.Bitmap
import com.aallam.underwave.image.Dimension
import com.aallam.underwave.image.ImageView
import com.aallam.underwave.image.dimension
import com.aallam.underwave.image.scale
import com.aallam.underwave.internal.view.impl.ImageViewManager
import com.aallam.underwave.load.impl.LoadRequest
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.lang.ref.WeakReference

internal class ViewManagerTest {

    @MockK
    lateinit var display: Dimension
    @RelaxedMockK
    lateinit var viewMap: MutableMap<ImageView, String>
    @RelaxedMockK
    lateinit var handler: Handler
    @RelaxedMockK
    lateinit var dimension: Dimension
    @RelaxedMockK
    lateinit var imageView: ImageView
    @RelaxedMockK
    lateinit var bitmap: Bitmap

    private val imageURL = "URL"

    private val imageViewRef: WeakReference<ImageView>
        get() = WeakReference(imageView)

    private val loadRequest: LoadRequest
        get() = LoadRequest(imageURL, imageViewRef)

    private val viewManager: ViewManager
        get() = ImageViewManager(display, viewMap, handler)

    @Before
    fun init() {
        MockKAnnotations.init(this)
        mockkStatic("com.aallam.underwave.image.DimensionKt")
        mockkStatic("com.aallam.underwave.image.BitmapKt")
    }

    @Test
    fun testLoadBitmapIntoImageView() {
        every { imageView.dimension } returns dimension
        every { bitmap.scale(dimension) } returns bitmap
        every { viewMap[imageView] } returns imageURL

        viewManager.loadBitmapIntoImageView(loadRequest, bitmap)

        verify { bitmap.scale(dimension) }
        verify { imageView.setImageBitmap(bitmap) }
    }

    @Test
    fun testHandler() {
        every { viewMap[imageView] } returns imageURL
        viewManager.postHandler(loadRequest, bitmap)
        verify { handler.post(any()) }
    }

    @Test
    fun testViewReused() {
        every { viewMap[imageView] } returns imageURL
        viewManager.isViewReused(loadRequest)
        verify { viewMap[imageView] }
    }

    @After
    fun finish() {
        unmockkAll()
    }
}
