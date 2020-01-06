package com.aallam.underwave.internal.async

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * Library global scope.
 */
internal expect class UnderwaveScope(
    dispatcher: CoroutineDispatcher,
    coroutineExceptionHandler: CoroutineExceptionHandler
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
}
