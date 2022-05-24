package com.fcossetta.pokedex.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.fcossetta.pokedex.databinding.MainFragmentBinding
import com.fcossetta.pokedex.main.data.PokemonEvent
import com.fcossetta.pokedex.main.data.PokemonViewModel
import com.fcossetta.pokedex.main.data.model.SimplePokemon
import dagger.hilt.android.AndroidEntryPoint
import io.uniflow.android.livedata.onEvents
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var _binding: MainFragmentBinding
    private lateinit var results: PagingData<SimplePokemon>

    private val viewModel: PokemonViewModel by activityViewModels()
    private lateinit var adapter: PokemonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (!::_binding.isInitialized) {
            _binding = MainFragmentBinding.inflate(inflater, container, false)
            adapter = PokemonAdapter(viewModel)
        }
        return _binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (_binding.recyclerView.adapter == null) {
            _binding.recyclerView.layoutManager = LinearLayoutManager(context)
            _binding.recyclerView.adapter = adapter
            adapter.addLoadStateListener { combinedLoadStates ->
                _binding.progressBar.isVisible = combinedLoadStates.refresh is LoadState.Loading
            }
        }
        onEvents(viewModel) {
            when (it) {
                is PokemonEvent.PokemonListFound -> {
                    lifecycleScope.launchWhenCreated {
                        it.pokemon.collect() { pokemons ->
                            adapter.submitData(pokemons)
                        }
                    }
                }
                is PokemonEvent.PokemonFound -> {
                    lifecycleScope.launch {
                        val mainToPokemonDetail =
                            MainFragmentDirections.mainToPokemonDetail(it.pokemon)
                        Navigation.findNavController(view).navigate(mainToPokemonDetail);
                    }
                }
            }
        }
    }

}