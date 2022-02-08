package com.example.androidschool.andersencoursework.di

import android.content.Context
import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.characters.CharactersDao
import com.example.androidschool.data.database.episodes.EpisodesDao
import com.example.androidschool.data.network.characters.CharactersService
import com.example.androidschool.data.network.episodes.EpisodesService
import com.example.androidschool.data.repositories.characters.CharactersLocalStorage
import com.example.androidschool.data.repositories.characters.CharactersRepositoryImpl
import com.example.androidschool.data.repositories.episodes.EpisodesRepositoryImpl
import com.example.androidschool.domain.characters.CharactersRepository
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import com.example.androidschool.domain.episode.EpisodesRepository
import com.example.androidschool.domain.episode.interactors.EpisodesInteractor
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
    fun provideCharactersRepository(
        service: CharactersService,
        localStorage: CharactersLocalStorage
    ): CharactersRepository {
        return CharactersRepositoryImpl(service, localStorage)
    }

    @Singleton
    @Provides
    fun provideCharactersService(retrofit: Retrofit): CharactersService {
        return retrofit.create(CharactersService::class.java)
    }

    @Singleton
    @Provides
    fun provideEpisodesInteractor(repository: EpisodesRepository): EpisodesInteractor {
        return EpisodesInteractor.Base(repository)
    }

    @Singleton
    @Provides
    fun provideEpisodesRepository(
        service: EpisodesService,
        dao: EpisodesDao,
        mapper: DatabaseMapper
    ): EpisodesRepository {
        return EpisodesRepositoryImpl(service, dao, mapper)
    }
}