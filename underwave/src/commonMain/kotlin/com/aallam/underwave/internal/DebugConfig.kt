package com.aallam.underwave.internal

import com.aallam.underwave.log.Logger

internal interface DebugConfig {

    val enabled: Boolean
    val logger: Logger

    object Default : DebugConfig {

        override val enabled = false
        override val logger: Logger = Logger.Default
    }
}
