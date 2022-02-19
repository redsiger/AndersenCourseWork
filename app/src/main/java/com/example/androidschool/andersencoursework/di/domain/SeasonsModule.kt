package com.example.androidschool.andersencoursework.di.domain

import com.example.androidschool.data.repositories.seasons.SeasonsRepositoryImpl
import com.example.androidschool.domain.seasons.interactor.SeasonsInteractor
import com.example.androidschool.domain.seasons.repository.SeasonsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SeasonsModule {

    @Singleton
    @Provides
    fun provideSeasonsInteractor(repository: SeasonsRepository): SeasonsInteractor =
        SeasonsInteractor.Base(repository)

    @Singleton
    @Provides
    fun provideSeasonsRepository(): SeasonsRepository =
        SeasonsRepositoryImpl()
}