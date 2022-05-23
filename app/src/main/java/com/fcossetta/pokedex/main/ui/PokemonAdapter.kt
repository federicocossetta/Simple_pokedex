package com.fcossetta.pokedex.main.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fcossetta.pokedex.R
import com.fcossetta.pokedex.databinding.PokemonItemListBinding
import com.fcossetta.pokedex.main.MainActivity
import com.fcossetta.pokedex.main.data.PokemonViewModel
import com.fcossetta.pokedex.main.data.model.SimplePokemon

class PokemonAdapter() :
    PagingDataAdapter<SimplePokemon, PokemonAdapter.MyViewHolder>(DiffUtilCallBack()) {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_item_list, parent, false)

        val binding = PokemonItemListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(context, binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class MyViewHolder(
        itemView: Context,
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
                if (pokemon.url != null && context != null) {

                    val pokemonViewModel =
                        ViewModelProviders.of(context as MainActivity)[PokemonViewModel::class.java]
                    pokemonViewModel.action {
                        TODO(
//                        sendEvent {
//                            PokemonEvent.PokemonDetailRequest(
//                                pokemon.url
//                            )
//                        }
                        )
                    }
                }
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