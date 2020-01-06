package com.aallam.underwave.internal.extension

import android.util.Log
import com.aallam.underwave.Underwave

private const val TAG = "Underwave"

/**
 * Log given message in [Log.DEBUG] mode.
 *
 * @param message the [String] to be logged.
 */
internal fun log(message: String) {
    if (Underwave.debug.enabled) {
        Underwave.debug.logger.log(Log.DEBUG, TAG, message)
    }
}

/**
 * Log given message and throwable in [Log.DEBUG] mode.
 *
 * @param message the [String] to log.
 * @param throwable exception to log.
 */
internal fun log(message: String, throwable: Throwable) {
    if (Underwave.debug.enabled) {
        val stackTrace = Log.getStackTraceString(throwable)
        Underwave.debug.logger.log(Log.DEBUG, TAG, "$message\n$stackTrace")
    }
}
