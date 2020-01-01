package com.aallam.underwave.internal.cache

/**
 * Cache that holds references to a number of values.
 */
internal interface Cache<K, V> {

    /**
     * Returns the value for [key] if it exists in the cache.
     * If a value was returned, it is moved to the head of the queue.
     * This returns null if a value is not cached.
     *
     * @param key the key to look for.
     * @return the value corresponding to [key] if exists, otherwise, returns null.
     */
    operator fun get(key: K): V?

    /**
     * Caches [value] for [key]. The value is moved to the head of the queue.
     */
    fun put(key: K, value: V)

    /**
     * Checks if a given [key] exists in the cache.
     *
     * @param key the key to check if exists.
     * @return true if the cache contains the given [key], otherwise, returns false.
     */
    fun contains(key: String): Boolean

    /**
     * Get current cache size
     *
     * @return cache current size in bytes.
     */
    fun size(): Long

    /**
     * Delete all entries and clear the cache.
     */
    fun clear()
}
