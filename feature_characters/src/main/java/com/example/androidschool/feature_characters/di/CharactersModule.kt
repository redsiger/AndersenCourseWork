package com.example.androidschool.feature_characters.di

import com.example.androidschool.feature_characters.data.CharactersRepositoryImpl
import com.example.androidschool.feature_characters.data.network.CharactersService
import com.example.androidschool.feature_characters.domain.CharactersRepository
import com.example.androidschool.feature_characters.domain.usecase.GetCharactersPagingUseCase
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class CharactersModule {

    @Singleton
    @Provides
    fun provideGetCharactersPagingUseCase(repository: CharactersRepository): GetCharactersPagingUseCase {
        return GetCharactersPagingUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideCharactersRepository(service: CharactersService): CharactersRepository {
        return CharactersRepositoryImpl(service)
    }


    @Singleton
    @Provides
    fun provideCharactersService(retrofit: Retrofit): CharactersService {
        return retrofit.create(CharactersService::class.java)
    }
}