package com.aallam.underwave.log

/**
 * May be used to create a custom logging solution to override the [Default] behaviour.
 */
expect interface Logger {

    /**
     * Pass the log details off to the [Logger] implementation.
     *
     * @param priority the priority/type of this log message
     * @param tag used to identify the source of a log message.
     * @param message the message to be logged.
     */
    fun log(priority: Int, tag: String, message: String)

    /**
     * Default implementation of [Logger].
     */
    object Default : Logger
}
