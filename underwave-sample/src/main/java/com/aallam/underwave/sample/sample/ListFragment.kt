package com.aallam.underwave.sample.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aallam.underwave.extension.load
import com.aallam.underwave.load.Request
import com.aallam.underwave.sample.Pokemon
import com.aallam.underwave.sample.R
import com.aallam.underwave.sample.extension.pokedex
import kotlinx.android.synthetic.main.fragment_item.view.*

class ListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = ItemAdapter(pokedex)
            }
        }
        return view
    }
}

class ItemAdapter(private val values: List<Pokemon>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = values[position]
        holder.bind(pokemon)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private var previousRequest: Request? = null

        fun bind(pokemon: Pokemon) {
            previousRequest?.cancel()
            this.view.pokeName.text = pokemon.name
            this.previousRequest = view.pokemonImage.load(pokemon.image)
        }
    }
}
