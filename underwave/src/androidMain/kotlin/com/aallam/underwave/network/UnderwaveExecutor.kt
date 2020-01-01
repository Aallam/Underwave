package com.aallam.underwave.network

import android.os.Process.THREAD_PRIORITY_BACKGROUND
import android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.math.min


/**
 * Decorated [ThreadPoolExecutor] for running jobs in Underwave.
 */
internal class UnderwaveExecutor(
    corePoolSize: Int = INITIAL_POOL_SIZE, maximumPoolSize: Int = MAXIMUM_THREAD_COUNT,
    keepAliveTime: Long = KEEP_ALIVE_TIME, unit: TimeUnit = KEEP_ALIVE_TIME_UNIT
) : ExecutorService by ThreadPoolExecutor(
    corePoolSize, maximumPoolSize, keepAliveTime, unit,
    LinkedBlockingQueue<Runnable>(), newThreadFactory()
) {
    companion object {
        private const val THREAD_NAME = "underwave_"
        private const val INITIAL_POOL_SIZE = 2
        private const val MAXIMUM_AUTOMATIC_THREAD_COUNT = 4
        private const val KEEP_ALIVE_TIME = 10L
        private val KEEP_ALIVE_TIME_UNIT: TimeUnit = TimeUnit.SECONDS

        /**
         * [ThreadFactory] that builds threads slightly above priority [THREAD_PRIORITY_BACKGROUND].
         */
        private fun newThreadFactory(): ThreadFactory = object : ThreadFactory {
            private var counter = 0
            @Synchronized
            override fun newThread(runnable: Runnable): Thread = Thread(runnable).apply {
                name = THREAD_NAME + counter++
                priority = THREAD_PRIORITY_BACKGROUND + THREAD_PRIORITY_MORE_FAVORABLE
            }
        }

        /**
         * Determines the number of cores available on the device.
         */
        private val MAXIMUM_THREAD_COUNT: Int
            get() = min(MAXIMUM_AUTOMATIC_THREAD_COUNT, Runtime.getRuntime().availableProcessors())
    }
}