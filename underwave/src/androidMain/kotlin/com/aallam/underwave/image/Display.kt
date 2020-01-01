package com.aallam.underwave.image

import android.util.DisplayMetrics

internal data class Display(val width: Int, val height: Int)

internal fun DisplayMetrics.toDisplay() = Display(widthPixels, heightPixels)