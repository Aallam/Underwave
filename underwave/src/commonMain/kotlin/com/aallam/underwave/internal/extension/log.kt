package com.aallam.underwave.internal.extension

/**
 * Log given message in debug mode.
 *
 * @param message the [String] to be logged.
 */
internal expect fun log(message: String)

/**
 * Log given message and throwable in [Log.DEBUG] mode.
 *
 * @param message the [String] to log.
 * @param throwable exception to log.
 */
internal expect fun log(message: String, throwable: Throwable)
