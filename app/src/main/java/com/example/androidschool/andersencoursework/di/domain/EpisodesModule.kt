package com.example.androidschool.andersencoursework.di.domain

import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.episodes.EpisodeDetailsDao
import com.example.androidschool.data.database.episodes.EpisodesListDao
import com.example.androidschool.data.network.episodes.EpisodesService
import com.example.androidschool.data.repositories.episodes.details.EpisodeDetailsLocalStorage
import com.example.androidschool.data.repositories.episodes.details.EpisodeDetailsRepositoryImpl
import com.example.androidschool.data.repositories.episodes.list.EpisodesListLocalStorage
import com.example.androidschool.data.repositories.episodes.list.EpisodesListRepositoryImpl
import com.example.androidschool.domain.episode.interactor.EpisodeDetailsInteractor
import com.example.androidschool.domain.episode.repository.EpisodesListRepository
import com.example.androidschool.domain.episode.interactor.EpisodesListInteractor
import com.example.androidschool.domain.episode.repository.EpisodeDetailsRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class EpisodesModule {

    @Singleton
    @Provides
    fun provideEpisodeDetailsInteractor(repository: EpisodeDetailsRepository): EpisodeDetailsInteractor =
        EpisodeDetailsInteractor.Base(repository)

    @Singleton
    @Provides
    fun provideEpisodeDetailsRepository(
        service: EpisodesService,
        localStorage: EpisodeDetailsLocalStorage
    ): EpisodeDetailsRepository =
        EpisodeDetailsRepositoryImpl(service, localStorage)


    @Singleton
    @Provides
    fun provideEpisodeDetailsLocalStorage(
        dao: EpisodeDetailsDao,
        mapper: DatabaseMapper
    ): EpisodeDetailsLocalStorage =
        EpisodeDetailsLocalStorage.Base(dao, mapper)

    @Singleton
    @Provides
    fun provideEpisodesInteractor(repository: EpisodesListRepository): EpisodesListInteractor =
        EpisodesListInteractor.Base(repository)

    @Singleton
    @Provides
    fun provideEpisodesRepository(
        service: EpisodesService,
        localStorage: EpisodesListLocalStorage
    ): EpisodesListRepository =
        EpisodesListRepositoryImpl(service, localStorage)

    @Singleton
    @Provides
    fun provideEpisodesListLocalStorage(
        dao: EpisodesListDao,
        mapper: DatabaseMapper
    ): EpisodesListLocalStorage =
        EpisodesListLocalStorage.Base(dao, mapper)

    @Singleton
    @Provides
    fun provideEpisodesService(retrofit: Retrofit): EpisodesService =
        retrofit.create(EpisodesService::class.java)
}