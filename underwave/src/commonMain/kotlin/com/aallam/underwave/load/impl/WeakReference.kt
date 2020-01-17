package com.aallam.underwave.load.impl

/**
 * Weak reference objects, which do not prevent their referents from
 * being finalized, and then reclaimed.
 */
expect class WeakReference<T : Any>(referred: T) {

    /**
     * Clears this reference object.
     */
    fun clear()

    /**
     * Returns this reference object's referent.  If this reference object has
     * been cleared, then this method returns null.
     *
     * @return The object to which this reference refers,
     * or null if this reference object has been cleared
     */
    fun get(): T?
}
