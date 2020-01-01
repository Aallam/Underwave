package com.aallam.underwave.storage.disk

import android.content.Context
import android.os.Environment
import java.io.File

/**
 * Check if media is mounted or storage is built-in, if so, try and use external cache directory
 * otherwise use internal cache dir.
 */
fun Context.getDiskCacheDirectory(uniqueName: String): File {
    val cachePath: String = getCachePath(this)
    return File("$cachePath${File.separator}$uniqueName")
}

/**
 * Get external cache path if available, otherwise get internal cache path.
 */
private fun getCachePath(context: Context): String {
    val isExternalAvailable: Boolean = isExternalStorageMounted || !isExternalStorageRemovable
    return if (isExternalAvailable) context.externalCacheDir!!.path else context.cacheDir.path
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
