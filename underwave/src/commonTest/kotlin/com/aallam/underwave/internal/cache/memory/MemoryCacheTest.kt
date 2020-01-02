package com.aallam.underwave.internal.cache.memory

import kotlin.test.Test

internal expect class MemoryCacheTest {

    @Test
    fun testGet()

    @Test
    fun testPut()

    @Test
    fun testGetNotExisting()

    @Test
    fun testSize()
}
