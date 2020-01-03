package com.aallam.underwave.sample.extension

import androidx.fragment.app.Fragment
import com.aallam.underwave.sample.UnderwaveSample
import kotlinx.serialization.UnstableDefault

@UnstableDefault
val Fragment.pokedex get() = (context?.applicationContext as UnderwaveSample).pokedex
