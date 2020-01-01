package com.aallam.underwave.extension

import android.util.Log
import com.aallam.underwave.Underwave.Companion.DEBUG

private const val TAG = "Underwave"

internal fun log(msg: String? = null, throwable: Throwable? = null) {
    if (DEBUG) Log.d(TAG, msg, throwable)
}
