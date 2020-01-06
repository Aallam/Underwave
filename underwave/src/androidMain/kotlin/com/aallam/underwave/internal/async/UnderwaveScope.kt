package com.aallam.underwave.internal.async

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

/**
 * Library global scope.
 */
internal actual class UnderwaveScope actual constructor(
    dispatcher: CoroutineDispatcher,
    coroutineExceptionHandler: CoroutineExceptionHandler
) : CoroutineScope {

    actual override val coroutineContext: CoroutineContext =
        SupervisorJob() + dispatcher + coroutineExceptionHandler

    /**
     * Creates a new [UnderwaveScope] object.
     */
    companion object {

        @JvmStatic
        fun newInstance(): UnderwaveScope {
            return UnderwaveScope(
                dispatcher = Dispatchers.Main,
                coroutineExceptionHandler = DefaultExceptionHandler
            )
        }
    }
}
