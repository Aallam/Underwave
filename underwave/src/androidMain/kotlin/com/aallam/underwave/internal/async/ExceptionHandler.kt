package com.aallam.underwave.internal.async

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName

/**
 * Default global coroutines exception handler.
 */
internal actual val DefaultExceptionHandler =
    CoroutineExceptionHandler { coroutineContext, throwable ->
        val coroutineName = coroutineContext[CoroutineName]?.name ?: "underwave"
        println("Error in $coroutineName: ${throwable.localizedMessage}")
    }
