package com.aallam.underwave.internal.cache

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class AndroidImageCacheTest : ImageCacheTest() {

    override val imageCache: ImageDataCache
        get() = ImageDataCache(
            memoryCache,
            diskCache
        )
}
