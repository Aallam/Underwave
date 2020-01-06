package com.aallam.underwave.load

import com.aallam.underwave.internal.image.ImageView
import com.aallam.underwave.load.impl.LoadRequest
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Job
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.lang.ref.WeakReference

internal actual class RequestTest {

    lateinit var request: Request
    @RelaxedMockK
    lateinit var job: Job

    @Before
    fun init() {
        MockKAnnotations.init(this)
        val imageView: ImageView = mockk()
        request = LoadRequest("url", WeakReference(imageView), job)
    }

    @Test
    actual fun testCancel() {
        every { job.isActive } returns true
        request.cancel()
        verify { job.cancel() }
    }

    @After
    fun finish() {
        unmockkAll()
    }
}
