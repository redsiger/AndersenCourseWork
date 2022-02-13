package com.example.androidschool.andersencoursework.di.domain

import com.example.androidschool.andersencoursework.di.domain.characters.CharactersModule
import com.example.androidschool.andersencoursework.di.domain.episodes.EpisodesModule
import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.episodes.EpisodesListDao
import com.example.androidschool.data.network.characters.CharactersService
import com.example.androidschool.data.network.episodes.EpisodesService
import com.example.androidschool.data.repositories.characters.detail.CharacterDetailsLocalStorage
import com.example.androidschool.data.repositories.characters.detail.CharacterDetailsRepositoryImpl
import com.example.androidschool.data.repositories.characters.list.CharactersLocalStorage
import com.example.androidschool.data.repositories.characters.list.CharactersListRepositoryImpl
import com.example.androidschool.data.repositories.episodes.list.EpisodesListLocalStorage
import com.example.androidschool.data.repositories.episodes.list.EpisodesRepositoryImpl
import com.example.androidschool.domain.characters.interactors.CharacterDetailsInteractor
import com.example.androidschool.domain.characters.repository.CharactersListRepository
import com.example.androidschool.domain.characters.interactors.CharactersListInteractor
import com.example.androidschool.domain.characters.repository.CharacterDetailsRepository
import com.example.androidschool.domain.episode.EpisodesListRepository
import com.example.androidschool.domain.episode.interactors.EpisodesListInteractor
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [CharactersModule::class, EpisodesModule::class])
class DomainModule