package com.aallam.underwave.internal.async

import com.aallam.underwave.internal.async.executor.LoadExecutor
import com.aallam.underwave.internal.async.executor.SourceExecutor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher

/**
 * Library coroutines dispatchers.
 */
internal actual object UnderwaveDispatchers {

    private val loadExecutor = LoadExecutor()
    private val sourceExecutor = SourceExecutor()

    /**
     * Default dispatcher, mainly for CPU heavy operations.
     */
    @JvmStatic
    actual val Default: CoroutineDispatcher = loadExecutor.asCoroutineDispatcher()

    /**
     * IO dispatcher, mainly for IO heavy operations.
     */
    @JvmStatic
    actual val IO: CoroutineDispatcher = sourceExecutor.asCoroutineDispatcher()
}
