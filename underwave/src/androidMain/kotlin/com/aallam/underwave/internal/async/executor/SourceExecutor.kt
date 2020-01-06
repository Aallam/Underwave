package com.aallam.underwave.internal.async.executor

import android.os.Process
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Executor for data fetching operations (IO heavy).
 */
internal class SourceExecutor : ExecutorService
by Executors.newCachedThreadPool({
    Thread(it).apply {
        name = "underwave_io"
        priority = Process.THREAD_PRIORITY_BACKGROUND + Process.THREAD_PRIORITY_MORE_FAVORABLE
    }
})
