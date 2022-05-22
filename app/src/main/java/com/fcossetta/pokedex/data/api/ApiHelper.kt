package com.fcossetta.pokedex.data.api

class ApiHelper(private val apiService: PokeService) {

    fun getPokemon(idOrName: String) = apiService.getPokemon(idOrName)
    suspend fun getPokemonList(limit: Int, offset: Int) = apiService.getPokemonList(limit, offset)
}