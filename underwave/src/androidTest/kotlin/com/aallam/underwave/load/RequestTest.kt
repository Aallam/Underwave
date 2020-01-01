package com.aallam.underwave.load

import com.aallam.underwave.image.ImageView
import com.aallam.underwave.load.impl.LoadRequest
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.lang.ref.WeakReference
import java.util.concurrent.Future

internal class RequestTest {

    @MockK
    lateinit var imageView: ImageView
    @RelaxedMockK
    lateinit var future: Future<*>

    private val request: Request
        get() = LoadRequest("URL", WeakReference(imageView)).apply {
            this.request = future
        }

    @Before
    fun init() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testCancel() {
        request.cancel()
        verify { future.cancel(any()) }
    }

    @After
    fun finish() {
        unmockkAll()
    }
}