package com.aallam.underwave.internal.async.executor

import android.os.Process
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.min

/**
 * Executor for loading and decoding operations (CPU heavy).
 */
internal class LoadExecutor : ExecutorService
by Executors.newFixedThreadPool(min(4, Runtime.getRuntime().availableProcessors()), {
    var threadNum = 0
    Thread(it).apply {
        name = "underwave_default_${threadNum++}"
        priority = Process.THREAD_PRIORITY_BACKGROUND + Process.THREAD_PRIORITY_MORE_FAVORABLE
    }
})
