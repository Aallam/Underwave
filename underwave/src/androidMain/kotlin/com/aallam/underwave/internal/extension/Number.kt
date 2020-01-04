package com.aallam.underwave.internal.extension

internal val Long.bytesToKilobytes: Int
    get() = (this / 1024).toInt()

internal val Int.bytesToKilobytes: Int
    get() = (this / 1024)
