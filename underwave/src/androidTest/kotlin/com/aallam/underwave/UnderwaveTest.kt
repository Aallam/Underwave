package com.aallam.underwave

import com.aallam.underwave.extension.MainCoroutineRule
import com.aallam.underwave.extension.runBlocking
import com.aallam.underwave.internal.cache.ImageDataCache
import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.internal.image.ImageView
import com.aallam.underwave.internal.network.impl.ImageDownloader
import com.aallam.underwave.internal.view.ViewManager
import com.aallam.underwave.load.impl.LoadRequest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal actual class UnderwaveTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    lateinit var underwave: Underwave
    @RelaxedMockK
    lateinit var imageCache: ImageDataCache
    @RelaxedMockK
    lateinit var downloader: ImageDownloader
    @RelaxedMockK
    lateinit var viewManager: ViewManager
    @RelaxedMockK
    lateinit var imageView: ImageView
    @RelaxedMockK
    lateinit var bitmap: Bitmap

    @Before
    fun init() {
        MockKAnnotations.init(this)
        underwave = Underwave(
            CoroutineScope(coroutineRule.testDispatcher),
            imageCache,
            downloader,
            viewManager
        )
    }

    @Test
    actual fun testLoadFromCache() {
        val url = "URL"
        coEvery { imageCache.get(url) } returns bitmap

        val load = underwave.load(url, imageView)

        verify { viewManager.viewMap[imageView] = url }
        coVerify { viewManager.load(load as LoadRequest, bitmap) }
    }

    @Test
    actual fun testLoadFromDistant() {
        val url = "URL"
        coEvery { imageCache.get(url) } returns null
        coEvery { downloader.download(url) } returns bitmap

        val load = underwave.load(url, imageView)

        verify { viewManager.viewMap[imageView] = url }
        coVerify { viewManager.load(load as LoadRequest, bitmap) }
    }

    @Test
    actual fun testInsertFromCache() = coroutineRule.runBlocking {
        val url = "URL"
        coEvery { imageCache.get(url) } returns bitmap

        val load = underwave.insert(url, imageView)

        verify { viewManager.viewMap[imageView] = url }
        coVerify { viewManager.load(load as LoadRequest, bitmap) }
    }

    @Test
    actual fun testInsertFromDistant() = coroutineRule.runBlocking {
        val url = "URL"
        coEvery { imageCache.get(url) } returns null
        coEvery { downloader.download(url) } returns bitmap

        val load = underwave.load(url, imageView)

        verify { viewManager.viewMap[imageView] = url }
        coVerify { viewManager.load(load as LoadRequest, bitmap) }
    }

    @Test
    actual fun testClear() {
        underwave.clear()

        coVerify { imageCache.clear() }
        coVerify { viewManager.viewMap.clear() }
    }

    @After
    fun finish() {
        unmockkAll()
    }
}
