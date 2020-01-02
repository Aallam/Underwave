package com.aallam.underwave.internal.async.dispatcher.impl

import com.aallam.underwave.internal.async.dispatcher.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Executor for loading and decoding operations (CPU heavy).
 */
internal object LoadExecutor : Dispatcher, ExecutorService
by Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()) {
    override val dispatcher: CoroutineDispatcher = asCoroutineDispatcher()
}
