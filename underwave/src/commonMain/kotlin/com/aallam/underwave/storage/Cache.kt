package com.aallam.underwave.storage

/**
 * Cache that holds references to a number of values.
 */
interface Cache<K, V> {

    /**
     * Returns the value for [key] if it exists in the cache.
     * If a value was returned, it is moved to the head of the queue.
     * This returns null if a value is not cached.
     */
    fun get(key: K): V?

    /**
     * Caches [value] for [key]. The value is moved to the head of the queue.
     */
    fun put(key: K, value: V)

    /**
     * Get current cache size
     */
    fun size(): Long

    /**
     * Delete all entries and clear the cache.
     */
    fun clear()
}