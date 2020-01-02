package com.aallam.underwave.sample

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import com.aallam.underwave.Underwave
import kotlinx.serialization.json.Json
import kotlinx.serialization.list

class UnderwaveSample : Application() {

    lateinit var pokedex: List<Pokemon>

    override fun onCreate() {
        strictMode()
        super.onCreate()
        this.pokedex = loadPokedex()
        Underwave.debug(true)
    }

    private fun strictMode() {
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectAll()
                .penaltyLog()
                .build()
        )
        StrictMode.setVmPolicy(
            VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
    }

    private fun loadPokedex(): List<Pokemon> {
        val jsonFile: String = applicationContext.assets.open(POKEDEX_JSON)
            .bufferedReader().use { it.readText() }
        return Json.parse(Pokemon.serializer().list, jsonFile)
    }

    companion object {
        private const val POKEDEX_JSON = "pokedex.json"
    }
}
