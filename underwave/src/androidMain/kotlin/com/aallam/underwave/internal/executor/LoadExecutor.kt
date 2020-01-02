package com.aallam.underwave.internal.executor

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Executor for loading and decoding operations (CPU heavy).
 */
object LoadExecutor : ExecutorService
by Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
