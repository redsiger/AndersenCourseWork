package com.example.androidschool.andersencoursework.di.domain.characters

import com.example.androidschool.data.network.characters.CharactersService
import com.example.androidschool.data.repositories.characters.detail.CharacterDetailsLocalStorage
import com.example.androidschool.data.repositories.characters.detail.CharacterDetailsRepositoryImpl
import com.example.androidschool.data.repositories.characters.list.CharactersListRepositoryImpl
import com.example.androidschool.data.repositories.characters.list.CharactersLocalStorage
import com.example.androidschool.domain.characters.interactors.CharacterDetailsInteractor
import com.example.androidschool.domain.characters.interactors.CharactersListInteractor
import com.example.androidschool.domain.characters.repository.CharacterDetailsRepository
import com.example.androidschool.domain.characters.repository.CharactersListRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class CharactersModule {

    @Singleton
    @Provides
    fun provideCharacterDetailsInteractor(repository: CharacterDetailsRepository): CharacterDetailsInteractor {
        return CharacterDetailsInteractor.Base(repository)
    }

    @Singleton
    @Provides
    fun provideCharacterDetailsRepository(
        service: CharactersService,
        localStorage: CharacterDetailsLocalStorage
    ): CharacterDetailsRepository {
        return CharacterDetailsRepositoryImpl(service, localStorage)
    }

    @Singleton
    @Provides
    fun provideCharactersInteractor(repository: CharactersListRepository): CharactersListInteractor {
        return CharactersListInteractor.Base(repository)
    }

    @Singleton
    @Provides
    fun provideCharactersRepository(
        service: CharactersService,
        localStorage: CharactersLocalStorage
    ): CharactersListRepository {
        return CharactersListRepositoryImpl(service, localStorage)
    }

    @Singleton
    @Provides
    fun provideCharactersService(retrofit: Retrofit): CharactersService {
        return retrofit.create(CharactersService::class.java)
    }
}