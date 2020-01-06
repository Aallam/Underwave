package com.aallam.underwave.internal.cache.disk

import android.content.Context
import android.os.Environment
import java.io.File

/**
 * Check if media is mounted or storage is built-in, if so, try and use external cache directory
 * otherwise use internal cache dir.
 */
internal fun Context.getDiskCacheDirectory(uniqueName: String = "underwave"): File {
    val cachePath: String = getCachePath()
    return File("$cachePath${File.separator}$uniqueName")
}

/**
 * Get external cache path if available, otherwise get internal cache path.
 */
private fun Context.getCachePath(): String {
    val isExternalAvailable: Boolean = isExternalStorageMounted || !isExternalStorageRemovable
    return if (isExternalAvailable) externalCacheDir!!.path else cacheDir.path
}

/**
 * Check if external storage is mounted.
 */
private val isExternalStorageMounted: Boolean
    get() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

/**
 * Check if the external storage is removable.
 */
private val isExternalStorageRemovable: Boolean
    get() = Environment.isExternalStorageRemovable()
