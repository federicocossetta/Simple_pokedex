package com.fcossetta.pokedex.main.data

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.fcossetta.pokedex.main.data.api.PokeService
import com.fcossetta.pokedex.main.data.model.Pokemon
import com.fcossetta.pokedex.main.data.repository.PagingSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import io.uniflow.android.AndroidDataFlow
import io.uniflow.core.flow.data.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.await
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(private val api: PokeService) :
    AndroidDataFlow(defaultState = UIState.Empty) {


    fun findPokemon(fullUrl: String?) = action {
        val response =
            withContext(Dispatchers.IO) { api.retrivePokemonData(fullUrl).await() }
        emitPokeDetail(response)

    }

    private fun emitPokeDetail(response: ResponseBody) {
        val rawPokemon: String? = response.string()
        if (rawPokemon != null) {
            val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val adapter = moshi.adapter(Pokemon::class.java)
            val pokemon: Pokemon? = adapter.fromJson(rawPokemon)
            if (pokemon != null) {
                action {
                    sendEvent { (PokemonEvent.PokemonFound(pokemon)) }
                }
            }
        }

    }

    fun getPokemonList(limit: Int) = action {
        var pager = Pager(
            config = PagingConfig(pageSize = 20, maxSize = 500), null,
            pagingSourceFactory = { PagingSource(api, limit) }
        )
        sendEvent {

            (PokemonEvent.PokemonListFound(pager.flow.cachedIn(viewModelScope)))
        }
    }
    fun setOnline(online: Boolean) = action {
        setState { NetworkState.NetworkChanged(online)  }
    }

}

