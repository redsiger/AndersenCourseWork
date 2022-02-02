package com.example.androidschool.andersencoursework.di.viewmodel

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

@Module
class ViewModelModule {

    @Provides
    fun provideViewModelDispatcher(): ViewModelDispatcher = ViewModelDispatcher.Base(Dispatchers.IO)
}