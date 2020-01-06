package com.aallam.underwave.internal.cache

import kotlin.test.Test

internal expect class ImageCacheTest {

    @Test
    fun testPut()

    @Test
    fun testGetFromMemory()

    @Test
    fun testGetFromDisk()

    @Test
    fun testClean()
}
