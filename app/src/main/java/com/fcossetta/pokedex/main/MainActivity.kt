package com.fcossetta.pokedex.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fcossetta.pokedex.R
import com.fcossetta.pokedex.main.data.NetworkState
import com.fcossetta.pokedex.main.data.PokemonEvent
import com.fcossetta.pokedex.main.data.PokemonViewModel
import com.fcossetta.pokedex.main.ui.LoadingFragmentDirections
import com.fcossetta.pokedex.main.ui.MainFragmentDirections
import com.fcossetta.pokedex.main.utils.NetworkListener
import dagger.hilt.android.AndroidEntryPoint
import io.uniflow.android.livedata.onEvents
import io.uniflow.android.livedata.onStates

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var recover: Boolean = false
    private lateinit var navController: NavController
    private lateinit var networkListener: NetworkListener

    val TAG = "TEST"

    private val viewModel: PokemonViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
        networkListener = NetworkListener(applicationContext, viewModel)
        viewModel.getPokemonList(100)
        onStates(viewModel) { state ->
            if (networkListener.online) {
                when (state) {
                    is NetworkState.NetworkChanged -> if (state.online) {
                        viewModel.getPokemonList(100)
                    }
                }
            } else {
                Toast.makeText(applicationContext, R.string.offline, Toast.LENGTH_SHORT).show()
                recover = true;
            }
        }
        onEvents(viewModel) { it ->
            when (it) {
                is PokemonEvent.PokemonFound -> {
                    val mainToPokemonDetail =
                        MainFragmentDirections.mainToPokemonDetail()
                    navController.navigate(mainToPokemonDetail)
                }
                is PokemonEvent.PokemonDetailRequest -> {

                    viewModel.findPokemon(it.pokemonUrl)
                }
                is PokemonEvent.PokemonListFound -> {
                    val mainToPokemonDetail =
                        LoadingFragmentDirections.actionLoadingToMain()
                    navController.navigate(mainToPokemonDetail)
                }
            }
        }
    }


}