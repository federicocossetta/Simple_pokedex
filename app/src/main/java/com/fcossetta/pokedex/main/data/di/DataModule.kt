package com.fcossetta.pokedex.main.data.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.fcossetta.pokedex.main.data.api.PokeService
import com.fcossetta.pokedex.main.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): PokeService = retrofit.create(PokeService::class.java)

    @Provides
    @Singleton
    fun provideRetrofitClient(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .build()
    }
    @Provides
    @Singleton
    fun provideGlide(@ApplicationContext context: Context?): RequestManager {
        return Glide.with(context!!)
    }

    @Provides
    @Singleton
    fun provideDefaultOkhttpClient(@ApplicationContext context: Context?): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
}