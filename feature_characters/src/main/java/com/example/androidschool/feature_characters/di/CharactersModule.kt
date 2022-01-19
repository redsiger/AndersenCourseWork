package com.example.androidschool.feature_characters.di

import com.example.androidschool.feature_characters.data.network.CharactersService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class CharactersModule {

    @Singleton
    @Provides
    fun provideCharactersService(retrofit: Retrofit): CharactersService {
        return retrofit.create(CharactersService::class.java)
    }
}