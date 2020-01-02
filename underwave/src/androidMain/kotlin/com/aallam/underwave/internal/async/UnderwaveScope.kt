package com.aallam.underwave.internal.async

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

internal class UnderwaveScope(
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    coroutineExceptionHandler: CoroutineExceptionHandler = DefaultExceptionHandler
) : CoroutineScope {
    override val coroutineContext = SupervisorJob() + dispatcher + coroutineExceptionHandler
}

internal val DefaultExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
    val coroutineName = coroutineContext[CoroutineName]?.name ?: "underwave-coroutine"
    println("Error in $coroutineName: ${throwable.localizedMessage}")
}
