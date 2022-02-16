package com.example.androidschool.andersencoursework.di.domain

import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.episodes.EpisodesListDao
import com.example.androidschool.data.network.episodes.EpisodesService
import com.example.androidschool.data.repositories.episodes.list.EpisodesListLocalStorage
import com.example.androidschool.data.repositories.episodes.list.EpisodesRepositoryImpl
import com.example.androidschool.domain.episode.EpisodesListRepository
import com.example.androidschool.domain.episode.interactors.EpisodesListInteractor
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class EpisodesModule {

    @Singleton
    @Provides
    fun provideEpisodesInteractor(repository: EpisodesListRepository): EpisodesListInteractor {
        return EpisodesListInteractor.Base(repository)
    }

    @Singleton
    @Provides
    fun provideEpisodesRepository(
        service: EpisodesService,
        localStorage: EpisodesListLocalStorage
    ): EpisodesListRepository {
        return EpisodesRepositoryImpl(service, localStorage)
    }

    @Singleton
    @Provides
    fun provideEpisodesListLocalStorage(
        dao: EpisodesListDao,
        mapper: DatabaseMapper
    ): EpisodesListLocalStorage {
        return EpisodesListLocalStorage.Base(dao, mapper)
    }

    @Singleton
    @Provides
    fun provideEpisodesService(retrofit: Retrofit): EpisodesService {
        return retrofit.create(EpisodesService::class.java)
    }
}