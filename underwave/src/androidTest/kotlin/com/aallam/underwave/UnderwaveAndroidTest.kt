package com.aallam.underwave

internal class UnderwaveAndroidTest : UnderwaveTest() {

    override val underwave: Underwave
        get() = Underwave(imageCache, downloader, viewManager)
}
