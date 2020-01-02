package com.aallam.underwave

import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.internal.image.ImageView
import com.aallam.underwave.internal.cache.ImageDataCache
import com.aallam.underwave.internal.network.impl.ImageDownloader
import com.aallam.underwave.internal.view.ViewManager
import com.aallam.underwave.load.impl.LoadRequest
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkClass
import io.mockk.unmockkAll
import io.mockk.verify
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal abstract class UnderwaveTest {

    private val imageURL: String = "URL"

    @RelaxedMockK
    lateinit var imageView: ImageView
    @RelaxedMockK
    lateinit var bitmap: Bitmap
    @MockK
    lateinit var imageCache: ImageDataCache
    @MockK
    lateinit var downloader: ImageDownloader
    @RelaxedMockK
    lateinit var viewManager: ViewManager

    abstract val underwave: Underwave

    @BeforeTest
    fun init() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testLoadFromCache() {
        every { imageCache[imageURL] } returns bitmap
        underwave.load(imageURL, imageView)
        verify { imageCache[imageURL] }
        verify { viewManager.loadBitmapIntoImageView(any(), bitmap) }
    }

    @Test
    fun testDownload() {
        val loadDataRequest = mockkClass(LoadRequest::class)
        every { imageCache[imageURL] } returns null
        every { downloader.download(any(), any()) } returns loadDataRequest
        underwave.load(imageURL, imageView)
        verify { downloader.download(any(), any()) }
    }

    @AfterTest
    fun finish() {
        unmockkAll()
    }
}
