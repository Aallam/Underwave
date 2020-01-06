package com.aallam.underwave.internal.cache.memory.bitmap

import android.graphics.BitmapFactory
import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.internal.image.Config
import java.lang.ref.SoftReference

/**
 * An Implementation of [BitmapPool] using a [Set] of [SoftReference].
 *
 * @param bitmapsSet a set to hold soft references to bitmaps.
 */
internal actual class BitmapDataPool(
    private val bitmapsSet: MutableSet<SoftReference<Bitmap>>
) : BitmapPool {

    override fun put(bitmap: Bitmap) {
        if (canBePooled(bitmap)) {
            bitmapsSet.add(SoftReference(bitmap))
        }
    }

    /**
     * Check if the bitmap can be pooled.
     */
    private fun canBePooled(bitmap: Bitmap): Boolean {
        return bitmap.isMutable && !bitmap.isRecycled
    }

    override fun addInBitmapOptions(options: BitmapFactory.Options) {
        // inBitmap only works with mutable bitmaps, so force the decoder to
        // return mutable bitmaps.
        options.inMutable = true
        // Try to find a bitmap to use for inBitmap.
        getReusableBitmap(options)?.let { inBitmap ->
            // If a suitable bitmap has been found, set it as the value of inBitmap.
            options.inBitmap = inBitmap
        }
    }

    /**
     * @param options - populated [BitmapFactory.Options]
     * @return Bitmap that case be used for inBitmap
     */
    private fun getReusableBitmap(options: BitmapFactory.Options): Bitmap? {
        if (bitmapsSet.isEmpty()) return null
        synchronized(bitmapsSet) {
            val iterator: MutableIterator<SoftReference<Bitmap>> = bitmapsSet.iterator()
            while (iterator.hasNext()) {
                iterator.next().get()?.let { item ->
                    if (item.isMutable) {
                        // Check to see it the item can be used for inBitmap.
                        if (item.canUseForInBitmap(options)) {
                            // Remove from reusable set so it can't be used again.
                            iterator.remove()
                            return item
                        }
                    } else {
                        // Remove from the set if the reference has been cleared.
                        iterator.remove()
                    }
                }
            }
        }
        return null
    }

    /**
     * From Android 4.4 (KitKat) onward we can re-use if the byte size of the new bitmap
     * is smaller than the reusable bitmap candidate allocation byte count.
     * @param targetOptions options that have the out* value populated
     * @return true if [Bitmap] can be used for inBitmap re-use with [targetOptions]
     */
    private fun Bitmap.canUseForInBitmap(
        targetOptions: BitmapFactory.Options
    ): Boolean {
        if (targetOptions.inSampleSize < 1) targetOptions.inSampleSize = 1
        val width = targetOptions.outWidth / targetOptions.inSampleSize
        val height = targetOptions.outHeight / targetOptions.inSampleSize
        val byteCount: Int = width * height * bytesPerPixel(config)
        return byteCount <= allocationByteCount
    }

    /**
     * Return the byte usage per pixel of a bitmap based on its configuration.
     */
    @Suppress("deprecation")
    private fun bytesPerPixel(config: Config): Int {
        return when (config) {
            Config.ARGB_8888 -> 4
            Config.RGB_565, Config.ARGB_4444 -> 2
            Config.ALPHA_8 -> 1
            else -> 1
        }
    }

    override val size: Int
        get() = bitmapsSet.size

    override fun clear() {
        bitmapsSet.clear()
    }

    companion object {

        /**
         * Creates a new [BitmapDataPool] object.
         */
        @JvmStatic
        fun newInstance(): BitmapDataPool {
            return BitmapDataPool(HashSet())
        }
    }
}
