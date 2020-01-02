package com.aallam.underwave.internal.async.dispatcher.impl

import com.aallam.underwave.internal.async.dispatcher.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Executor for data fetching operations (IO heavy).
 */
internal object SourceExecutor : Dispatcher, ExecutorService
by Executors.newCachedThreadPool() {
    override val dispatcher: CoroutineDispatcher = asCoroutineDispatcher()
}
