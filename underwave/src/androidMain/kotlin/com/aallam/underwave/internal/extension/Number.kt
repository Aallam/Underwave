package com.aallam.underwave.internal.extension

val Long.bytesToKilobytes: Int
    get() = (this / 1024).toInt()

val Int.bytesToKilobytes: Int
    get() = (this / 1024)

val Int.kilobytesToBytes: Long
    get() = this * 1024L
