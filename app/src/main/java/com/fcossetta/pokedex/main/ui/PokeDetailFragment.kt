package com.fcossetta.pokedex.main.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.fcossetta.pokedex.databinding.FragmentPokemonDetailBinding
import com.fcossetta.pokedex.main.data.PokemonViewModel
import com.fcossetta.pokedex.main.data.model.Pokemon
import com.fcossetta.pokedex.main.data.model.StatInfo
import com.fcossetta.pokedex.main.data.model.Type
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PokeDetailFragment : Fragment() {
    val pokeDetailFragmentArgs: PokeDetailFragmentArgs by navArgs()
    private lateinit var _binding: FragmentPokemonDetailBinding
    @Inject
    lateinit var requestManager: RequestManager

    //    private var _binding: Any
    private val viewModel: PokemonViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updatePokemonInformation(pokeDetailFragmentArgs.pokemon, context)


    }

    @SuppressLint("SetTextI18n")
    private fun updatePokemonInformation(pokemon: Pokemon?, context: Context?) {
        if (pokemon != null && context != null) {
            _binding.pokemonName.text = pokemon.name?.capitalize(Locale.ROOT)
            val padStart = pokemon.id.toString().padStart(3, '0')
            _binding.pokemonNumber.text = "#$padStart"
            requestManager
                .load(pokemon.sprites?.front_default)
                .into(_binding.pokemonImage)
            if (pokemon.stats != null) {
                for (item: StatInfo in pokemon.stats) {
                    val name: String? = item.stat!!.name
                    when (name) {
                        "hp" -> _binding.hp.text = item.baseStats?.toString()
                        "attack" -> _binding.attack.text = item.baseStats?.toString()
                        "defense" -> _binding.defense.text = item.baseStats?.toString()
                        "special-attack" -> _binding.specialAttack.text = item.baseStats?.toString()
                        "special-defense" -> _binding.specialDefense.text =
                            item.baseStats?.toString()
                        "speed" -> _binding.speed.text = item.baseStats?.toString()
                    }
                }
            }
            pokemon.weight?.let { _binding.weight.text = it.toString() }
            pokemon.height?.let { _binding.height.text = it.toString() }
            if (pokemon.types != null) {
                val ids = mutableListOf<Int>()
                for (item: Type in pokemon.types) {

                    val cardView = CardView(context)
                    cardView.id = View.generateViewId()
                    cardView.cardElevation = 10F
                    cardView.layoutParams =
                        RelativeLayout.LayoutParams(200, ViewGroup.LayoutParams.WRAP_CONTENT)
                    cardView.radius = 10F
                    val typeText = TextView(context)
                    typeText.layoutParams =
                        RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    typeText.gravity = Gravity.CENTER
                    typeText.text = item.typeDetail?.name
                    cardView.addView(typeText)
                    _binding.container.addView(cardView)
                    ids.add(cardView.id)
                }
                _binding.typesFlow.referencedIds = ids.toIntArray()
            }
        }
    }
}
