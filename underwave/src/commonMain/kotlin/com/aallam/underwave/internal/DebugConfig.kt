package com.aallam.underwave.internal

import com.aallam.underwave.log.Logger

internal expect interface DebugConfig {

    val enabled: Boolean
    val logger: Logger

    object Default : DebugConfig
}
