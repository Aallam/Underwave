package com.aallam.underwave.internal.network.impl

import android.graphics.BitmapFactory
import android.net.TrafficStats
import com.aallam.underwave.internal.async.UnderwaveDispatchers
import com.aallam.underwave.internal.extension.log
import com.aallam.underwave.internal.image.Bitmap
import com.aallam.underwave.internal.network.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

internal class BitmapHttpClient(
    private val dispatcher: CoroutineDispatcher
) : HttpClient<Bitmap> {

    override suspend fun get(url: String): Bitmap? = withContext(dispatcher) {
        var urlConnection: HttpURLConnection? = null
        try {
            TrafficStats.setThreadStatsTag(USER_REQUEST_TAG)
            urlConnection = URL(url).openConnection() as HttpURLConnection
            URL(url)
            val inputStream = urlConnection.inputStream.buffered()
            BitmapFactory.decodeStream(inputStream)
        } catch (ex: IOException) {
            log("Error while downloading $this", ex)
            null
        } finally {
            urlConnection?.disconnect()
            TrafficStats.clearThreadStatsTag()
        }
    }

    companion object {

        private const val USER_REQUEST_TAG = 0xF00D

        /**
         * Create new [BitmapHttpClient] object.
         */
        @JvmStatic
        fun newInstance(): BitmapHttpClient {
            return BitmapHttpClient(UnderwaveDispatchers.IO)
        }
    }
}
