package com.aallam.underwave.log

import android.util.Log
import com.aallam.underwave.log.Logger.Default

/**
 * May be used to create a custom logging solution to override the [Default] behaviour.
 */
actual interface Logger {

    /**
     * Pass the log details off to the [Logger] implementation.
     *
     * @param priority the priority/type of this log message
     * @param tag used to identify the source of a log message.
     * @param message the message to be logged.
     */
    actual fun log(priority: Int, tag: String, message: String)

    /**
     * Default implementation of [Logger] which uses [Log.println] to log the messages.
     */
    actual object Default : Logger {

        override fun log(priority: Int, tag: String, message: String) {
            Log.println(priority, tag, message)
        }
    }
}
