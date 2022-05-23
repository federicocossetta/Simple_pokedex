package com.fcossetta.pokedex.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.fcossetta.pokedex.R
import com.fcossetta.pokedex.databinding.MainFragmentBinding
import com.fcossetta.pokedex.main.data.PokemonEvent
import com.fcossetta.pokedex.main.data.PokemonViewModel
import com.fcossetta.pokedex.main.data.model.SimplePokemon
import dagger.hilt.android.AndroidEntryPoint
import io.uniflow.android.livedata.onEvents
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var _binding: MainFragmentBinding
    private lateinit var results: PagingData<SimplePokemon>
    val TAG = "TEST"

    private val viewModel: PokemonViewModel by activityViewModels()
    private val adapter = PokemonAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val inflate = inflater.inflate(R.layout.main_fragment, container, false)
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return inflate
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.context = requireContext()
        _binding.recyclerView.adapter = adapter
        onEvents(viewModel) {
            when (it) {
                is PokemonEvent.PokemonListFound -> {
                    lifecycleScope.launch {
                        it.pokemon.collect() { pokemons ->
                            adapter.submitData(pokemons)
                        }
                    }
                }
            }
        }
    }

}