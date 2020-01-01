package com.aallam.underwave.sample

import android.app.Application
import com.aallam.underwave.Underwave
import kotlinx.serialization.json.Json
import kotlinx.serialization.list

class UnderwaveSample : Application() {

    lateinit var pokedex: List<Pokemon>

    override fun onCreate() {
        super.onCreate()
        this.pokedex = loadPokedex()
        Underwave.debug(true)
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
