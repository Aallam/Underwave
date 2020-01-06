package com.aallam.underwave.internal.network

/**
 * Client for HTTP requests.
 */
internal interface HttpClient<T> {

    /**
     * GET Http request to [url].
     *
     * @param url URL to be called.
     * @return request result if any, otherwise return null.
     */
    suspend fun get(url: String): T?
}
