package com.example.androidschool.andersencoursework.di

import android.content.Context
import com.example.androidschool.data.database.CharactersDao
import com.example.androidschool.data.database.CharactersStorage
import com.example.androidschool.data.network.CharactersService
import com.example.androidschool.data.repositories.CharactersRepositoryImpl
import com.example.androidschool.domain.characters.CharactersRepository
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class DomainModule {

    @Singleton
    @Provides
    fun provideCharactersInteractor(repository: CharactersRepository): CharactersInteractor {
        return CharactersInteractor.Base(repository)
    }

    @Singleton
    @Provides
    fun provideCharactersRepository(service: CharactersService, storage: CharactersStorage, context: Context): CharactersRepository {
        return CharactersRepositoryImpl(service, storage, context)
    }

    @Singleton
    @Provides
    fun provideCharactersService(retrofit: Retrofit): CharactersService {
        return retrofit.create(CharactersService::class.java)
    }
}