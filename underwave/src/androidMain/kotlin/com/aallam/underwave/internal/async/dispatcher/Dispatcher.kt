package com.aallam.underwave.internal.async.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import java.util.concurrent.ExecutorService

internal interface Dispatcher : ExecutorService {

    /**
     * Coroutine dispatcher.
     */
    val dispatcher: CoroutineDispatcher
}