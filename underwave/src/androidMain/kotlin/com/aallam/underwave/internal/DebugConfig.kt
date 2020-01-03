package com.aallam.underwave.internal

import com.aallam.underwave.log.Logger

internal actual interface DebugConfig {

    actual val enabled: Boolean
    actual val logger: Logger

    actual object Default : DebugConfig {

        override val enabled = false
        override val logger: Logger = Logger.Default
    }
}
