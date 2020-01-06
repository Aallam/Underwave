package com.aallam.underwave.internal.view.impl

import com.aallam.underwave.internal.view.Loader
import kotlinx.coroutines.CoroutineDispatcher

/**
 * An implementation of [Loader].
 *
 * @param dispatcher coroutine dispatcher
 */
internal expect class BitmapLoader(
    dispatcher: CoroutineDispatcher
) : Loader
