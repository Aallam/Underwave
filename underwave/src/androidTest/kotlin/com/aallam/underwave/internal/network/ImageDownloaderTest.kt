package com.aallam.underwave.internal.network

import com.aallam.underwave.image.Dimension
import com.aallam.underwave.image.ImageView
import com.aallam.underwave.internal.cache.ImageCache
import com.aallam.underwave.internal.network.impl.ImageDownloader
import com.aallam.underwave.internal.view.ViewManager
import com.aallam.underwave.load.impl.LoadRequest
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.lang.ref.WeakReference
import java.util.concurrent.Future
import kotlin.test.assertEquals

internal class ImageDownloaderTest {

    @MockK
    lateinit var executorService: NetworkExecutor
    @MockK
    lateinit var imageCache: ImageCache
    @MockK
    lateinit var viewManager: ViewManager
    @MockK
    lateinit var dimension: Dimension
    @MockK
    lateinit var imageView: WeakReference<ImageView>

    private val loadRequest: LoadRequest
        get() = LoadRequest("URL", imageView)

    private val downloader: Downloader
        get() = ImageDownloader(imageCache, viewManager, executorService)

    @Before
    fun init() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testDownload() {
        val request = loadRequest
        val req = mockk<Future<*>>()
        every { executorService.submit(any()) } returns req
        downloader.download(request, dimension)
        assertEquals(req, request.request)
    }

    @Test
    fun testShutdown() {
        every { executorService.shutdown() } just Runs
        downloader.shutdown()
        verify { executorService.shutdown() }
    }

    @After
    fun finish() {
        unmockkAll()
    }
}
