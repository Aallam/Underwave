package com.aallam.underwave.internal.network

import com.aallam.underwave.extension.MainCoroutineRule
import com.aallam.underwave.extension.runBlocking
import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.internal.network.impl.BitmapHttpClient
import com.aallam.underwave.internal.network.impl.ImageDownloader
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal actual class ImageDownloaderTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var downloader: ImageDownloader
    @MockK
    lateinit var bitmapHttpClient: BitmapHttpClient

    @Before
    fun init() {
        MockKAnnotations.init(this)
        downloader = ImageDownloader(bitmapHttpClient)
    }

    @Test
    actual fun testDownload() = coroutineRule.runBlocking {
        val url = "URL"
        val bitmap = mockk<Bitmap>()
        coEvery { bitmapHttpClient.get(url) } returns bitmap
        val result = downloader.download(url)
        coVerify { bitmapHttpClient.get(url) }
        assertEquals(bitmap, result)
    }

    @After
    fun finish() {
        unmockkAll()
    }
}
