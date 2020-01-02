package com.aallam.underwave.internal.async

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Library coroutines dispatchers.
 */
internal expect object UnderwaveDispatchers {

    /**
     * Default dispatcher, mainly for CPU heavy operations.
     */
    val Default: CoroutineDispatcher

    /**
     * IO dispatcher, mainly for IO heavy operations.
     */
    val IO: CoroutineDispatcher
}
