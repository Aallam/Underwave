package com.aallam.underwave

import kotlin.test.Test

internal expect class UnderwaveTest {

    @Test
    fun testLoadFromCache()

    @Test
    fun testLoadFromDistant()

    @Test
    fun testInsertFromCache()

    @Test
    fun testInsertFromDistant()

    @Test
    fun testClear()
}
