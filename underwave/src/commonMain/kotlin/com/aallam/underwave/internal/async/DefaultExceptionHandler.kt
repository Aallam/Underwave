package com.aallam.underwave.internal.async

import kotlinx.coroutines.CoroutineExceptionHandler

/**
 * Default global coroutines exception handler.
 */
internal expect val DefaultExceptionHandler: CoroutineExceptionHandler
