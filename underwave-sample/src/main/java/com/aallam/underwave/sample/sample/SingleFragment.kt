package com.aallam.underwave.sample.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aallam.underwave.Underwave
import com.aallam.underwave.sample.R
import com.aallam.underwave.sample.extension.pokedex
import kotlinx.android.synthetic.main.fragment_single.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingleFragment : Fragment() {

    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_single, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadButton.setOnClickListener {
            context?.let { context ->
                uiScope.launch {
                    val pokemon = pokedex.random()
                    pokemonName.text = pokemon.name
                    Underwave.with(context).insert(pokemon.image, pokemonImage)
                }
            }
        }
    }
}
