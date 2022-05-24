package com.fcossetta.pokedex.main.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fcossetta.pokedex.databinding.PokemonItemListBinding
import com.fcossetta.pokedex.main.data.PokemonViewModel
import com.fcossetta.pokedex.main.data.model.SimplePokemon

class PokemonAdapter constructor(val pokemonViewModel: PokemonViewModel) :
    PagingDataAdapter<SimplePokemon, PokemonAdapter.MyViewHolder>(DiffUtilCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = PokemonItemListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class MyViewHolder(
        var _binding: PokemonItemListBinding,
    ) : RecyclerView.ViewHolder(_binding.root) {

        fun bind(pokemon: SimplePokemon) {
            pokemon.name?.let {
                _binding.pokemonNameCompact.text = it.capitalize()
            }
            val pokenumber = pokemon.url?.let {
                it.substring(it.indexOf("pokemon/") + 8, pokemon.url.length - 1).padStart(3, '0')
            }
            _binding.pokemonNumberCompact.text = "#$pokenumber"
            itemView.setOnClickListener {
                pokemonViewModel.findPokemon(pokemon.url)
            }
        }
    }
}


class DiffUtilCallBack : DiffUtil.ItemCallback<SimplePokemon>() {
    override fun areItemsTheSame(
        oldItem: SimplePokemon,
        newItem: SimplePokemon,
    ): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: SimplePokemon,
        newItem: SimplePokemon,
    ): Boolean {
        return oldItem == newItem
    }
}