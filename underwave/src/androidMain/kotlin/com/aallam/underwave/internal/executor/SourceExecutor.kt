package com.aallam.underwave.internal.executor

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Executor for data fetching operations (IO heavy).
 */
object SourceExecutor : ExecutorService
by Executors.newCachedThreadPool()
