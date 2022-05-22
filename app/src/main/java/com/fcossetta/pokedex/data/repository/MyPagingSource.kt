package com.fcossetta.pokedex.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fcossetta.pokedex.data.api.PokeService
import com.fcossetta.pokedex.main.data.model.PokemonResult
import com.fcossetta.pokedex.main.data.model.SimplePokemon
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.await

class MyPagingSource(
    private val api: PokeService, private val limit: Int,
) : PagingSource<Int, SimplePokemon>() {
    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, SimplePokemon> {
        var values: List<SimplePokemon> = emptyList()
        try {
            if (params.key != null) {
                val test = params.key?.minus(2)?.times(limit);
                if (test != null) {
                    val result = api.getPokemonList(limit, test).await()
                    val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                    val adapter: JsonAdapter<PokemonResult>? =
                        moshi.adapter(PokemonResult::class.java)
                    val jsonString = result.string()
                    val fromJson = adapter?.fromJson(jsonString)
                    if (fromJson?.results != null)
                        values = fromJson.results
                }
            }
        } catch (e: Exception) {
            Error("Error", e)

        }
        return PagingSource.LoadResult.Page(
            data = values,
            prevKey = params.key,
            nextKey = params.key?.plus(1) ?: STARTING_PAGE_INDEX.plus(1)
        )
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, SimplePokemon>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}