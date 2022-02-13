package com.example.androidschool.andersencoursework.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
class CoroutinesScopesModule {

    @Named("Dispatchers.IO")
    @Provides
    fun provideDispatcherIO(): CoroutineDispatcher = Dispatchers.IO

}