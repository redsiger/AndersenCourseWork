package com.example.androidschool.andersencoursework.di.util

import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UIMapperModule {

    @Singleton
    @Provides
    fun provideUIMapper(): UIMapper = UIMapper()
}