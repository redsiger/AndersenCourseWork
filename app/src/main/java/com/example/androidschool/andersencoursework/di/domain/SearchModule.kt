package com.example.androidschool.andersencoursework.di.domain

import com.example.androidschool.domain.characters.repository.CharactersListRepository
import com.example.androidschool.domain.episode.repository.EpisodesListRepository
import com.example.androidschool.domain.search.SearchInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SearchModule {

    @Singleton
    @Provides
    fun provideSearchInteractor(
        characterRepository: CharactersListRepository,
        episodesRepository: EpisodesListRepository
    ): SearchInteractor =
        SearchInteractor.Base(characterRepository, episodesRepository)
}