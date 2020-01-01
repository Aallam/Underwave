package com.aallam.underwave

import kotlin.test.Test
import kotlin.test.assertTrue

class UnderwaveAndroidTest {
    @Test
    fun testHello() {
        assertTrue("Android" in Underwave.PLATFORM)
    }
}